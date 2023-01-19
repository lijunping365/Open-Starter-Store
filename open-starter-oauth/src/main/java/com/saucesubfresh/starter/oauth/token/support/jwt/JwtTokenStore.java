/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.oauth.token.support.jwt;


import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.properties.OAuthProperties;
import com.saucesubfresh.starter.oauth.properties.token.TokenProperties;
import com.saucesubfresh.starter.oauth.token.AbstractTokenStore;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenEnhancer;
import com.saucesubfresh.starter.oauth.utils.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Jwt token store，把生成的 jwt token 传给客户端
 *
 * @author lijunping
 */
@Slf4j
public class JwtTokenStore extends AbstractTokenStore {

    private final OAuthProperties oauthProperties;

    public JwtTokenStore(TokenEnhancer tokenEnhancer, OAuthProperties oauthProperties) {
        super(tokenEnhancer, oauthProperties);
        this.oauthProperties = oauthProperties;
    }

    @Override
    public AccessToken doGenerateToken(Authentication authentication) {
        final TokenProperties tokenProperties = oauthProperties.getToken();
        AccessToken token = new AccessToken();
        long now = System.currentTimeMillis();
        long accessTokenExpiredTime = getAccessTokenExpiredTime(now);
        String userDetailsStr = JSON.toJSON(authentication.getUserDetails());
        Claims claims = Jwts.claims().setSubject(userDetailsStr);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(accessTokenExpiredTime))
                .signWith(Keys.hmacShaKeyFor(tokenProperties.getSecretKeyBytes()), SignatureAlgorithm.HS256)
                .compact();
        token.setExpiredTime(String.valueOf(accessTokenExpiredTime));
        token.setAccessToken(accessToken);

        if (supportRefreshToken()){
            long refreshTokenExpiredTime = getRefreshTokenExpiredTime(now);
            String refreshToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(refreshTokenExpiredTime))
                    .signWith(Keys.hmacShaKeyFor(tokenProperties.getSecretKeyBytes()), SignatureAlgorithm.HS256)
                    .compact();
            token.setRefreshToken(refreshToken);
        }

        return token;
    }

    @Override
    public Authentication readAuthentication(String refreshToken) {
        return null;
    }

    @Override
    public void invalidateAccessToken(String accessToken) {
        throw new UnsupportedOperationException("jwt access token not support delete");
    }

    @Override
    public void invalidateRefreshToken(String refreshToken) {
        throw new UnsupportedOperationException("jwt refresh token not support delete");
    }
}
