package com.lightcode.starter.oauth.token.support.jdbc;

import com.lightcode.starter.oauth.authentication.Authentication;
import com.lightcode.starter.oauth.token.AccessToken;
import com.lightcode.starter.oauth.token.TokenStore;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: JDBC token store，把 token 及 token 的映射保存到数据库中
 */
@Slf4j
public class JdbcTokenStore implements TokenStore {

    @Override
    public Authentication readAuthentication(String accessToken) {
        return null;
    }

    @Override
    public AccessToken generateToken(Authentication authentication) {
        return null;
    }


}