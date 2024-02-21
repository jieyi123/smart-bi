package com.pjieyi.smartbi.service;

import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static com.pjieyi.smartbi.utils.SMSUtils.sendMessage;

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

    @Test
    void testAliyunSMS(){
        sendMessage("originai","SMS_464995252","","888888");
    }


}