package org.example.helpers;

import org.example.TokenUtils.TokenUtil;
import org.example.constant.AuthErrorEnum;
import org.example.domain.User;
import org.example.exception.ConditionException;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserVerifyTokenHelper {

    @Autowired
    private UserService userService;

    /**
     * 通过 token 获取 user id
     * @return Long user id
     */
    public long getCurrentUserIdByToken() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = servletRequestAttributes.getRequest().getHeader("token");
        Long userId =  TokenUtil.verifyJwtTokenWithUserId(token);

        return userId;
    }

    /**
     * 通过 token 获取 user id 并且在 数据库中查找 该 user id 是否存在
     * @return
     */
    //
    public long verifyUserIdByToken() {
        long userId = getCurrentUserIdByToken();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getMessage());
        }

        return userId;
    }



}
