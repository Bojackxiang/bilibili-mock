package org.example.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 这个是放在 controller 上面的，并祈求要求传进来一个 String Array
 * example:
 * @ApiLimitRole(limitRoleCodeList = {Constants.ROLE_CODE_LV0})
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface DataLimit {
    // 这是一个无参数的 切面方法
}
