package com.saucesubfresh.starter.oauth.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 用户名密码登录
 */
@Data
@Accessors(chain = true)
public class PasswordLoginRequest extends BaseLoginRequest implements Serializable {

    private static final long serialVersionUID = 2512743958725643646L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
