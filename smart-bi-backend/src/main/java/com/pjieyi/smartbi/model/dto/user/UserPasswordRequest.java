package com.pjieyi.smartbi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pjieyi
 * @description 修改密码请求体
 */
@Data
public class UserPasswordRequest implements Serializable {

    private static final long serialVersionUID = 139080967857677642L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 原密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 确认密码
     */
    private String confirmPassword;
}
