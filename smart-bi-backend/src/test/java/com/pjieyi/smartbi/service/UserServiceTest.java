package com.pjieyi.smartbi.service;

import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.model.entity.User;
import com.pjieyi.smartbi.utils.AliyunOssUtil;
import com.pjieyi.smartbi.utils.SMSUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 用户服务测试
 *
 * @author pjieyi
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        boolean result = userService.updateById(user);
        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteUser() {
        boolean result = userService.removeById(1L);
        Assertions.assertTrue(result);
    }

    @Test
    void testGetUser() {
        User user = userService.getById(1L);
        Assertions.assertNotNull(user);
    }


    @Test
    void retrievePassword() {
        String userPassword="123456789";
        String checkPassword="123456789";
        String phone="13232323232";
        String verifyCode="1231";
        try {
            long id = userService.retrievePassword(userPassword, checkPassword, phone, verifyCode);
            Assertions.assertTrue(id>0);
        }catch (BusinessException e){
            e.printStackTrace();
        }
    }

    @Resource
    private SMSUtils smsUtils;
    @Test
    void testAliyunSMS(){
        smsUtils.sendMessage("originai","SMS_464995252","","888888");
    }

    @Resource
    private AliyunOssUtil aliyunOssUtil;

    @Test
    void testOss(){
        String filePath= "C:\\Users\\pjy17\\Pictures\\Screenshots\\屏幕截图 2023-06-02 111203.png";
        File file=new File(filePath);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            aliyunOssUtil.upload(filePath,inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}