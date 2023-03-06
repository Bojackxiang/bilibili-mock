package org.example.TokenUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.constant.AuthErrorEnum;
import org.example.exception.ConditionException;
import org.example.utils.RSAUtil;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {
    public static final String ISSUER = "Issuer";


    public static String generateToken(Long userId) {
        Algorithm algorithm;
        Calendar calendar = Calendar.getInstance();
        try {
            algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 30);
        Date expireDate = calendar.getTime();

        return JWT.create()
                .withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static Long verifyJwtTokenWithUserId(String token) {
        Algorithm algorithm;
        try {
            algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String userId = decodedJWT.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_TOKEN_EXPIRE.getCode(),
                    AuthErrorEnum.AUTH_ERROR_TOKEN_EXPIRE.getMessage()
            );
        } catch (Exception e) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_TOKEN_VERIFY.getCode(),
                    AuthErrorEnum.AUTH_ERROR_TOKEN_VERIFY.getMessage()
            );
        }


    }
}