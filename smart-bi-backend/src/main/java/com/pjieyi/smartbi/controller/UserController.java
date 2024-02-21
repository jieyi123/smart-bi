package com.pjieyi.smartbi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.pjieyi.smartbi.common.BaseResponse;
import com.pjieyi.smartbi.common.DeleteRequest;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.common.ResultUtils;
import com.pjieyi.smartbi.model.dto.response.CaptureResponse;
import com.pjieyi.smartbi.model.dto.user.*;
import com.pjieyi.smartbi.model.entity.User;
import com.pjieyi.smartbi.model.vo.UserVO;
import com.pjieyi.smartbi.service.UserService;
import com.pjieyi.smartbi.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户接口
 * @author pjieyi
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String phone=userRegisterRequest.getPhone();
        String verifyCode = userRegisterRequest.getVerifyCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,phone,verifyCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,phone,verifyCode);
        return ResultUtils.success(result);
    }



    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userLoginRequest.getType().equals("account")) {
            String userAccount = userLoginRequest.getUserAccount();
            String userPassword = userLoginRequest.getUserPassword();
            if (StringUtils.isAnyBlank(userAccount, userPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User user = userService.userLogin(userAccount, userPassword, request);
            return ResultUtils.success(user);
        }else{ //手机号登录
            String phone = userLoginRequest.getPhone();
            String captcha = userLoginRequest.getCaptcha();
            if (StringUtils.isAnyBlank(phone,captcha)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User user=userService.userLogin(request,phone,captcha);
            return ResultUtils.success(user);

        }
    }


    /**
     * 发送验证码
     * @param phone 手机号
     * @return
     */
    @GetMapping("/captcha")
    public BaseResponse getCaptcha(@RequestParam String phone){
        if (StringUtils.isAnyBlank(phone)){
            throw new BusinessException((ErrorCode.PARAMS_ERROR));
        }
        userService.getCaptcha(phone);
        return ResultUtils.success(phone);
    }

    /**
     * 图片二次验证
     * @param getParams 验证参数
     * @return
     */
    @GetMapping("/verifyCapture")
    public BaseResponse verifyCapture(@RequestParam Map<String,String> getParams){
        CaptureResponse captureResponse = userService.identifyCapture(getParams);
        return ResultUtils.success(captureResponse);
    }

    /**
     * 找回密码
     * @param retrievePasswordRequest
     * @return
     */
    @PostMapping("/retrievePassword")
    public BaseResponse retrievePassword(@RequestBody RetrievePasswordRequest retrievePasswordRequest){
        if (retrievePasswordRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验参数
        String userPassword = retrievePasswordRequest.getUserPassword();
        String checkPassword = retrievePasswordRequest.getCheckPassword();
        String phone = retrievePasswordRequest.getPhone();
        String verifyCode = retrievePasswordRequest.getVerifyCode();
        if (StringUtils.isAnyBlank(userPassword,checkPassword,phone,verifyCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.retrievePassword(userPassword, checkPassword, phone, verifyCode);
        return ResultUtils.success(userId);
    }

    /**
     * 修改密码
     * @param passwordRequest
     * @return 用户id
     */
    @PostMapping("/updatePassword")
    public BaseResponse updatePassword(@RequestBody UserPasswordRequest passwordRequest){
        if (passwordRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验参数
        Long id = passwordRequest.getId();
        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getNewPassword();
        String confirmPassword = passwordRequest.getConfirmPassword();
        if (StringUtils.isAnyBlank(oldPassword,newPassword,confirmPassword) ||id==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updatePassword(id,oldPassword,newPassword,confirmPassword);
        return ResultUtils.success(id);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("用户注销")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserVO> getUserById(int id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest, HttpServletRequest request) {

        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    /**
     * 分页获取用户列表
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }

        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        String userAccount = userQuery.getUserAccount();
        String userRole = userQuery.getUserRole();
        String userName = userQuery.getUserName();
        String phone = userQuery.getPhone();
        String email = userQuery.getEmail();
        String startTime = userQueryRequest.getStartTime();
        String endTime = userQueryRequest.getEndTime();
        Integer gender = userQuery.getGender();
        //模糊查询
        if (StringUtils.isNotEmpty(userAccount)){
            queryWrapper.like(User::getUserAccount,userAccount);
        }
        if (StringUtils.isNotEmpty(userName)){
            queryWrapper.like(User::getUserName,userName);
        }
        if (StringUtils.isNotEmpty(phone)){
            queryWrapper.like(User::getPhone,phone);
        }
        if (StringUtils.isNotEmpty(email)){
            queryWrapper.like(User::getEmail,email);
        }
        if(gender != null){
            queryWrapper.like(User::getGender,gender);
        }
        if (StringUtils.isNotEmpty(userRole)){
            queryWrapper.like(User::getUserRole,userRole);
        }
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNoneEmpty(endTime)){
            //大于等于
            queryWrapper.ge(User::getCreateTime,startTime);
            //小于等于
            queryWrapper.le(User::getCreateTime,endTime);
        }
        queryWrapper.orderByDesc(User::getUpdateTime);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    // endregion
}
