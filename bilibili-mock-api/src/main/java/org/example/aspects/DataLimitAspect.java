package org.example.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.Configs.AppConfig;
import org.example.domain.UserMoment;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Order(1)
@Component
@Aspect
public class DataLimitAspect {



    // 切点：我们要在什么时候开始注入
    // 下面这个 方法应该是强制的，并且告诉 aop 我要是用哪一个 annotation
    @Pointcut("@annotation(org.example.annotation.DataLimit)")
    public void check() {
        System.out.println("======>> check() <<<======");
    }


    // 这是一个无参的切面方法，所以我们要在这个方法里面获取到我们的参数
    @Before("check()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("======>> doBefore() <<<======");
        Object[] args = joinPoint.getArgs();
        // arg 是 payload 里面的数据
        for(Object arg : args){
            if(arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment) arg;
                String type = userMoment.getType();
                System.out.println("======>> type: " + type + " <<<======");
            }
        }

    }
}
