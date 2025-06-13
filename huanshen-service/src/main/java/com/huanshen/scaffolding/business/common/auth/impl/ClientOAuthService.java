package com.huanshen.scaffolding.business.common.auth.impl;

import com.huanshen.scaffolding.common.utils.SignatureUtil;
import com.huanshen.scaffolding.common.exception.CommonAssert;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.security.auth.AuthService;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.constant.SecurityConsts;
import com.huanshen.scaffolding.security.domain.LoginUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * TokenAuthService
 *
 * @date 2024-04-03 11:08
 */
@Component
public class ClientOAuthService implements AuthService {

//    @Autowired
//    private TokenConfigProperties tokenConfigProperties;
//    @Autowired
//    private SmspClientAuthService clientAuthService;

    private long expireTime = 5*60*1000L;
    private String secretKey;

    @Override
    public String getAccount(HttpServletRequest request) {
        String signature = request.getHeader(SecurityConsts.REQUEST_HEADER_SIGNATURE);
        if (signature == null) {
            return null;
        }
        String ipAddr = request.getRemoteHost();

        SignatureUtil.SignatureDto signatureDto = null;
        try {
            signatureDto = SignatureUtil.decodeSignature(signature);
        } catch (Exception e) {
            CommonAssert.error(ExceptionEnums.INVALID_TOKEN);
        }
        String username = signatureDto.getUsername();


        //过期限制
        Long timestamp = signatureDto.getTimestamp();
        CommonAssert.isFalse(System.currentTimeMillis() - timestamp > expireTime, ExceptionEnums.INVALID_TOKEN);

        //密码校验
        String password = ""; //select from db
//        SmspClientAuth clientAuth = clientAuthService.select(username, ClientAuthTypeEnum.OAUTH.getCode(), ipAddr);
//        CommonAssert.notNull(clientAuth, ExceptionEnums.NO_PERMISSION);

//        String password = clientAuth.getPassword();
//        password = AesCipherUtils.INS.deCrypto(password);

        String inputSha = signatureDto.getSha();
        String currentSha = SignatureUtil.createSha(secretKey, signatureDto.getUsername(), password, signatureDto.getTimestamp());
        CommonAssert.equals(inputSha, currentSha, ExceptionEnums.WRONG_ACCOUNT_PASSWD);
        return username;
    }

    @Override
    public LoginUser getLoginUser(AuthTypeEnum authType, String account) {
        LoginUser loginUser = new LoginUser();
        loginUser.setAccount(account);
        loginUser.setUserName(account);
        loginUser.setIsOwner(false);
        loginUser.setLang(getLang());
        return loginUser;
    }

    @Override
    public AuthTypeEnum type() {
        return AuthTypeEnum.CLIENT_OAUTH;
    }
}
