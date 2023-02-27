package org.example.helpers;

import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.example.TokenUtils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserVerifyTokenHelper {
    public long getCurrentUserIdByToken() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = servletRequestAttributes.getRequest().getHeader("token");
        Long userId =  TokenUtil.verifyJwtTokenWithUserId(token);

        return userId;

    }

}
