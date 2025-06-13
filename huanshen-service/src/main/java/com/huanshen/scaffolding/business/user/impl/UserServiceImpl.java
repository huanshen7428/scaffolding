package com.huanshen.scaffolding.business.user.impl;

import com.huanshen.scaffolding.business.user.IUserService;
import com.huanshen.scaffolding.business.user.test.TUserInfo;
import com.huanshen.scaffolding.business.user.test.TUserInfoMapper;
import com.huanshen.scaffolding.common.dynamic.DataSource;
import com.huanshen.scaffolding.common.dynamic.DataSourceType;
import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.security.utils.AesCipherUtils;
import com.huanshen.scaffolding.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * UserServiceImpl
 *
 */
//@Service
public class UserServiceImpl implements IUserService {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TUserInfoMapper userInfoMapper;

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public LoginUser login(String account, String password) {

        TUserInfo userInfo = userInfoMapper.selectById(1);


        long expireTime = 1L;
        TimeUnit timeUnit = TimeUnit.DAYS;

        //用户信息校验
        //密码正确
//        String dbPassword = "";
//        String cPassword = AesCipherUtils.INS.enCrypto(password);
//        CommonAssert.equals(cPassword, dbPassword, ExceptionEnums.WRONG_ACCOUNT_PASSWD);

        String token = JwtUtils.INS.sign(account);

        LoginUser loginUser = new LoginUser();
        loginUser.setToken(AesCipherUtils.INS.enCrypto(token));
        loginUser.setUserName("admin");
//        loginUser.setRoleName("");
        loginUser.setIsOwner(true);

        //将用户信息LoginUser存入redis
//        redisTemplate.opsForValue().set(SecurityConsts.LOGIN_TOKEN_KEY + account, JSONObject.toJSONString(loginUser), expireTime, timeUnit);

        return loginUser;
    }
}
