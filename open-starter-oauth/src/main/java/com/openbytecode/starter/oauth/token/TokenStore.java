/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.oauth.token;

import com.openbytecode.starter.oauth.authentication.Authentication;

/**
 * token store
 *
 * @author lijunping
 */
public interface TokenStore {

    /**
     * 生成 access_token 和 refresh_token
     * @param authentication
     * @return
     */
    AccessToken generateToken(Authentication authentication);

    /**
     * 根据 refreshToken 重新生成 access_token 和 refresh_token
     * @param refreshToken
     * @return
     */
    AccessToken refreshToken(String refreshToken);

    /**
     * 使 accessToken 失效
     * @param accessToken
     */
    void invalidateAccessToken(String accessToken);

    /**
     * 使 refreshToken 失效
     * @param refreshToken
     */
    void invalidateRefreshToken(String refreshToken);

}
