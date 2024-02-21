package com.pjieyi.smartbi.model.dto.response;

import lombok.Data;

import java.util.Map;

/**
 * @author pjieyi
 * @description  图形验证响应参数
 */
@Data
public class CaptureResponse {
    private String result;
    private String reason;
    private String status;
    private Map<String,String> captchaArgs;
}
