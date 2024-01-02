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
package com.saucesubfresh.starter.openai.interceptor;

import com.saucesubfresh.starter.openai.properties.OpenAIProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author lijunping
 */
@Slf4j
public class OpenAiAuthInterceptor implements Interceptor {

    private final OpenAIProperties openAIProperties;

    public OpenAiAuthInterceptor(OpenAIProperties openAIProperties) {
        this.openAIProperties = openAIProperties;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = openAIProperties.getToken();
        Request request = chain.request()
                .newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }
}
