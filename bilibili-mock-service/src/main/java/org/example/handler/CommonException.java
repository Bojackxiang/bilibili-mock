package org.example.handler;

import org.example.dao.domain.JsonResponse;
import org.example.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e){
        String errMsg = e.getMessage();
        if(e instanceof ConditionException){
            String errCode = ((ConditionException) e).getCode();
            return JsonResponse.fail(errCode, errMsg);
        }else {
            return JsonResponse.fail("500", errMsg);
        }
    }

}
