package com.saucesubfresh.starter.oauth.token.support.jdbc;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.token.AbstractTokenStore;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenEnhancer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: JDBC token store，把 token 及 token 的映射保存到数据库中
 */
@Slf4j
public class JdbcTokenStore extends AbstractTokenStore {

    public JdbcTokenStore(TokenEnhancer tokenEnhancer) {
        super(tokenEnhancer);
    }

    @Override
    public AccessToken doGenerateToken(Authentication authentication) {
        return null;
    }

}
