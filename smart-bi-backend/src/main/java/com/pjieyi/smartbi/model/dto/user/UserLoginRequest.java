package com.pjieyi.smartbi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author pjieyi
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String phone;
    private String captcha;
    //登录方式  mobile手机号  account账户
    private String type;
}
