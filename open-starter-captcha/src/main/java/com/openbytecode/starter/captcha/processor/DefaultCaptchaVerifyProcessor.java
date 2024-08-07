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
package com.openbytecode.starter.captcha.processor;

import com.openbytecode.starter.captcha.exception.InvalidArgumentException;
import com.openbytecode.starter.captcha.exception.InvalidValidateCodeException;
import com.openbytecode.starter.captcha.exception.ValidateCodeExpiredException;
import com.openbytecode.starter.captcha.repository.CaptchaRepository;
import com.openbytecode.starter.captcha.exception.ValidateCodeException;
import com.openbytecode.starter.captcha.request.CaptchaVerifyRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping
 */
public class DefaultCaptchaVerifyProcessor implements CaptchaVerifyProcessor{

    private final CaptchaRepository captchaRepository;

    public DefaultCaptchaVerifyProcessor(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public void validate(CaptchaVerifyRequest request) throws ValidateCodeException{
        if (StringUtils.isBlank(request.getRequestId())){
            throw new InvalidArgumentException("RequestId must not be empty or null");
        }

        String validateCode = captchaRepository.get(request.getRequestId());
        String codeInRequest = request.getCode();
        if (StringUtils.isBlank(validateCode)) {
            throw new ValidateCodeExpiredException("The validate code has expired");
        }
        if (!StringUtils.equals(validateCode, codeInRequest)) {
            throw new InvalidValidateCodeException("The validate code input error");
        }
    }
}
