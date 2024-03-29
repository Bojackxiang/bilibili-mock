package org.example.service;

import com.alibaba.fastjson.JSONObject;
import org.example.TokenUtils.TokenUtil;
import org.example.constant.AuthErrorEnum;
import org.example.constant.UserConstants;
import org.example.domain.PageResults;
import org.example.domain.RefreshToken;
import org.example.domain.User;
import org.example.dao.UserDao;
import org.example.domain.UserInfo;
import org.example.dao.UserInfoDao;
import org.example.exception.ConditionException;
import org.example.helper.UserAuthHelper;
import org.example.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoDao userInfoDao;

    public UserService() {
    }

    public Map<String, Object> loginForDps(User user) {
        boolean validPhoneNumber = UserAuthHelper.validatePhone(user);
        if (!validPhoneNumber) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_INVALID_PHONE.getCode(),
                    AuthErrorEnum.AUTH_ERROR_INVALID_PHONE.getMessage());
        }

        // HANDLE IF USER NOT EXISTED
        User existedUser = UserAuthHelper.getUserByPhone(user, userDao);
        if (existedUser == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getMessage());
        }

        // COMPARE THE PASSWORD AND GENERATE THE TOKEN
        String ra3wPassword = UserAuthHelper.parseUserPassword(user.getPassword());
        String md5Password = MD5Util.sign(ra3wPassword, existedUser.getSalt(), "UTF-8");
        if (!md5Password.equals(existedUser.getPassword())) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_INVALID_PASSWORD.getCode(),
                    AuthErrorEnum.AUTH_ERROR_INVALID_PASSWORD.getMessage());
        }

        // 生成马上要使用的 token 和 refresh token （refresh token 能够保存七天）
        String accessToken = TokenUtil.generateToken(existedUser.getId());
        String refreshToken = TokenUtil.generateRefreshToken(existedUser.getId());

        // 将 refresh token 和 userId 一起存到 database 中
        Map<String, Object> result = this.deleteAndSaveUserRefreshToken(existedUser.getId(), refreshToken, accessToken);

        return result;
    }

    private Map<String, Object> deleteAndSaveUserRefreshToken(Long userId, String refreshToken, String accessToken) {
        // DELETE user's user token
        // ADD user's generate refresh token
        userDao.deleteUserRefreshToken(refreshToken);
        userDao.addUserRefreshToken(userId, refreshToken, new Date());

        Map<String, Object> result = new HashMap<>();
        result.put("refreshToken", refreshToken);
        result.put("accessToken", accessToken);

        return result;

    }

    public void createUser(User user) {
        // CHECK VALID PHONE
        boolean validRequest = false;
        boolean validPhoneNumber = UserAuthHelper.validatePhone(user);
        if (!validPhoneNumber) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_INVALID_PHONE.getCode(),
                    AuthErrorEnum.AUTH_ERROR_INVALID_PHONE.getMessage());
        }
        // CHECK EXISTED USER
        User existedUser = UserAuthHelper.getUserByPhone(user, userDao);
        if (existedUser != null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_ALREADY_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_ALREADY_EXISTED.getMessage());
        }

        // INSERT USER
        Date now = new Date();
        String salt = now.getTime() + "";
        String encodedPassword = user.getPassword();
        String rawPassword = UserAuthHelper.parseUserPassword(encodedPassword);
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        // INSERT USER INTO DATABASE
        userDao.addUser(user);
        // INSERT DATA INTO USER INFO TABLE
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setGender(UserConstants.GENDER_UNKNOWN);
        userInfo.setBirth(UserConstants.DEFAULT_BIRTHDAY);
        userInfo.setNick(UserConstants.DEFAULT_NICKNAME);
        userInfo.setCreateTime(new Date());
        userDao.addUserInfo(userInfo);

    }

    public String generateToken(User user) {
        // VALID USER PHONE
        boolean validPhoneNumber = UserAuthHelper.validatePhone(user);
        if (!validPhoneNumber) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_INVALID_PHONE.getCode(),
                    AuthErrorEnum.AUTH_ERROR_INVALID_PHONE.getMessage());
        }

        // HANDLE IF USER NOT EXISTED
        User existedUser = UserAuthHelper.getUserByPhone(user, userDao);
        if (existedUser == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getMessage());
        }

        // COMPARE THE PASSWORD AND GENERATE THE TOKEN
        String ra3wPassword = UserAuthHelper.parseUserPassword(user.getPassword());
        String md5Password = MD5Util.sign(ra3wPassword, existedUser.getSalt(), "UTF-8");
        if (!md5Password.equals(existedUser.getPassword())) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_INVALID_PASSWORD.getCode(),
                    AuthErrorEnum.AUTH_ERROR_INVALID_PASSWORD.getMessage());
        }

        String token = TokenUtil.generateToken(existedUser.getId());

        return token;
    }

    public User getUserInfoByUserId(Long userId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getMessage());
        }
        System.out.println("ger user info by user id userId = " + userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        System.out.println("userInfo = " + userInfo.toString());
        user.setUserInfo(userInfo);

        return user;
    }

    public UserInfo updateUserInfo(UserInfo userInfoInput) {
        if (userInfoInput == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_INFO_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_INFO_NOT_EXISTED.getMessage());
        }

        // UPDATE USER INFO
        System.out.println("userInfoInput.toString() = " + userInfoInput.toString());
        userInfoInput.setUpdateTime(new Date());
        userInfoDao.updateUserInfo(userInfoInput);
        return userInfoInput;
    }

    public User getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    public void updateUserById(User user) {
        Date updatedTime = new Date();
        System.out.println("user = " + user.toString());
        user.setUpdateTime(updatedTime);
        userDao.updateUserById(user);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userInfoDao.getUserInfoByUserIds(userIdList);
    }

    public PageResults<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        Long userId = params.getLong("userId");
        String nick = params.getString("nick");

        params.put("start", (no - 1) * size);
        params.put("limit", size);

        Integer total = userDao.pageCountUserInfo(params);
        List<UserInfo> userInfoList = new ArrayList<>();
        if (total > 0) {
            userInfoList = userInfoDao.pageListUserInfo(params);
        }

        return new PageResults<>(total, userInfoList);


    }


    public void logout(String refreshToken, Long userId) {
        userDao.deleteUserRefreshToken(refreshToken);
    }

    public String refreshAccessToken(String refreshToken, long userId) {
        RefreshToken refreshTokenDetail = userDao.getRefreshToken(refreshToken);
        if(refreshTokenDetail == null){
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_REFRESH_TOKEN_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_REFRESH_TOKEN_NOT_EXISTED.getMessage());

        }

        Long refreshTokenUserId = refreshTokenDetail.getUserId();
        return TokenUtil.generateRefreshToken(userId);
    }
}
