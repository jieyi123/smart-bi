package com.pjieyi.smartbi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pjieyi.smartbi.model.dto.response.CaptureResponse;
import com.pjieyi.smartbi.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户服务
 *
 * @author pjieyi
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param phone 手机号
     * @param verifyCode 验证码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String phone,String verifyCode);


    /**
     * 图片二次验证
     * @param getParams 验证参数
     * @return 图形验证响应参数
     */
    CaptureResponse identifyCapture(Map<String, String> getParams);

    /**
     * 找回密码
     * @param userPassword 新密码
     * @param checkPassword 确认密码
     * @param phone 电话
     * @param verifyCode 验证码
     * @return 用户id
     */
    long retrievePassword(String userPassword,String checkPassword,String phone,String verifyCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 手机登录
     * @param request
     * @param phone 手机号
     * @param captcha 验证码
     * @return
     */
    User userLogin(HttpServletRequest request,String phone, String captcha );

    /**
     * 获取验证码
     * @param phone 手机号
     * @return
     */
    String getCaptcha(String phone);

    /**
     * 修改密码
     * @param id 用户id
     * @param oldPass 旧密码
     * @param newPass 新密码
     * @param confirmPass 确认密码
     * @return
     */
     boolean updatePassword(Long id,String oldPass,String newPass,String confirmPass);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     *
     * @param user 原始信息
     * @return 脱敏后的用户信息
     */
    public  User getSafetyUser(User user);
}
