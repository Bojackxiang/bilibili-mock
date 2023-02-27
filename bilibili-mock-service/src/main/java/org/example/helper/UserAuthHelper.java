package org.example.helper;

import com.mysql.cj.util.StringUtils;
import org.example.constant.AuthErrorEnum;
import org.example.dao.domain.User;
import org.example.dao.domain.UserDao;
import org.example.exception.ConditionException;
import org.example.utils.RSAUtil;

public class UserAuthHelper {

    private UserDao userDao;

    public UserAuthHelper(UserDao userDao) {
        this.userDao = userDao;
    }

    // validate if user has a valid phone
    public static boolean validatePhone(User user) {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            return false;
        }
        return true;
    }


    public static User getUserByPhone(User user, UserDao userDao){
        return userDao.getUserByPhone(user.getPhone());
    }

    public static User getUserById(User user, UserDao userDao){
        return userDao.getUserById(user.getId());
    }

    public static String parseUserPassword(String encryptedPassword){
        String rawPassword = "";
        try {
            rawPassword = RSAUtil.decrypt(encryptedPassword);
        } catch (Exception e) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_UNABLE_DECRYPT_PASSWORD.getCode(),
                    AuthErrorEnum.AUTH_ERROR_UNABLE_DECRYPT_PASSWORD.getMessage());
        }
        return rawPassword;

    }



}
