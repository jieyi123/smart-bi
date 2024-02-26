package com.pjieyi.smartbi.manager;

import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pjieyi
 * @desc 用于对接AI平台
 * 示例：https://github.com/liyupi/yucongming-java-sdk
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient client;

    /**
     *
     * @param message 问题
     * @param modelId 模型id
     * @return 结果
     */
    public String doChart(String message,Long modelId){
        //构造请求参数
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        //获取响应结果
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        if (response==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI响应错误");
        }
        if (response.getData()==null){
            return "";
        }
        return response.getData().getContent();
    }
}
