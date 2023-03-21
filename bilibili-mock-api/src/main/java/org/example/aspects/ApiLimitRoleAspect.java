package org.example.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.Configs.AppConfig;
import org.example.annotation.ApiLimitRole;
import org.example.domain.authorities.UserRole;
import org.example.exception.ConditionException;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ApiLimitRoleAspect {
    @Autowired
    private UserVerifyTokenHelper userVerifyTokenHelper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AppConfig appConfig;

    // 切点：我们要在什么时候开始注入
    // 下面这个 方法应该是强制的，并且告诉 aop 我要是用哪一个 annotation
    @Pointcut("@annotation(org.example.annotation.ApiLimitRole)")
    public void check() {
        System.out.println("======>> check() <<<======");
    }

    // 下面的 check 是执行上面的 check 方法
    // apiLimitRole apiLimitRole 是我们自己定义的一个 annotation
    // apiLimitedRole 可以看作是 annotation 后的一个实例化传禁区
    @Before("check() && @annotation(apiLimitRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitRole apiLimitRole) {
        System.out.println("======>> doBefore() <<<======");
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 17;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        System.out.println("here");
        //获取到 外面穿进来的 limited Role list 的值
        String[] limitedRoleCodeList = apiLimitRole.limitRoleCodeList();

        // 将 limited role code list 转换为 set
        Set<String> limitedRoleCodeSet = Arrays
                .stream(limitedRoleCodeList)
                .collect(Collectors.toSet());


        // role code set 和 limited Role Code Set 求交集
        try {
            // 将数据库里获取的用户权限 list 变成 set
            List<String> userRoleCodeList = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toList());
            Set<String> userRoleCodeSet = userRoleCodeList.stream().collect(Collectors.toSet());
            System.out.println("userRoleCodeSet = " + userRoleCodeSet);
            System.out.println("limitedRoleCodeSet = " + limitedRoleCodeSet);
            userRoleCodeSet.retainAll(limitedRoleCodeSet);
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new ConditionException("intersection 错误");
        }


    }


}
