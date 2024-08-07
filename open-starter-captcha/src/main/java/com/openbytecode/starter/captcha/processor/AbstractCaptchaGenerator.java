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

import com.openbytecode.starter.captcha.core.sms.ValidateCode;
import com.openbytecode.starter.captcha.exception.InvalidArgumentException;
import com.openbytecode.starter.captcha.repository.CaptchaRepository;
import com.openbytecode.starter.captcha.exception.ValidateCodeException;
import com.openbytecode.starter.captcha.request.CaptchaGenerateRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 验证码抽象处理器，包含验证码的生成处理，保存处理
 *
 * @author lijunping
 */
@Slf4j
public abstract class AbstractCaptchaGenerator<T extends ValidateCode> implements CaptchaGenerator<T> {

  private final CaptchaRepository captchaRepository;

  public AbstractCaptchaGenerator(CaptchaRepository captchaRepository) {
    this.captchaRepository = captchaRepository;
  }

  @Override
  public T create(CaptchaGenerateRequest request) throws ValidateCodeException {
    if (StringUtils.isBlank(request.getRequestId())){
      throw new InvalidArgumentException("RequestId must not be empty or null");
    }

    T validateCode = this.generate();
    this.repository(request.getRequestId(), validateCode);
    return validateCode;
  }

  private void repository(String requestId, T validateCode){
    captchaRepository.save(requestId, validateCode);
  }

  protected abstract T generate() throws ValidateCodeException;
}
