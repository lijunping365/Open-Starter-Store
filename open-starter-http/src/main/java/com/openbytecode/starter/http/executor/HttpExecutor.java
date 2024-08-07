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
package com.openbytecode.starter.http.executor;


import com.openbytecode.starter.http.exception.HttpException;
import com.openbytecode.starter.http.request.HttpRequest;

/**
 * @author lijunping
 */
public interface HttpExecutor {

    /**
     * 执行 http 请求
     * @param httpRequest
     * @return
     * @throws HttpException
     */
    String execute(HttpRequest httpRequest) throws HttpException;
}
