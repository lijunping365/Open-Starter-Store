package com.lightcode.starter.captcha.core.sms;

import com.lightcode.starter.captcha.generator.ValidateCodeGenerator;
import com.lightcode.starter.captcha.properties.CaptchaProperties;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public class SmsCodeGenerator implements ValidateCodeGenerator {

    private final CaptchaProperties captchaProperties;

    public SmsCodeGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Override
    public ValidateCode generate() {
        String code = RandomStringUtils.randomNumeric(captchaProperties.getSms().getLength());
        return new ValidateCode(code, captchaProperties.getSms().getExpireTime());
    }
}
