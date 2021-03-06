package com.saucesubfresh.starter.oauth.token;

import com.saucesubfresh.starter.oauth.authentication.Authentication;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: token store
 */
public interface TokenStore {

    /**
     * 生成 access_token 和 refresh_token
     * @param authentication
     * @return
     */
    AccessToken generateToken(Authentication authentication);

}
