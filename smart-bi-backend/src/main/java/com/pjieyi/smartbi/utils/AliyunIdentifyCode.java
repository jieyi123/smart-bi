package com.pjieyi.smartbi.utils;

import lombok.Data;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author pjieyi
 * @description 图形验证
 */
@Component
@Data
@ConfigurationProperties("aliyun.captcha")
public class AliyunIdentifyCode {
    //1.验证参数信息
    private  String captchaId; //appId
    private  String captchaKey; //appKey
    private  String domain;

    public  JSONObject getParams(Map<String,String> getParams){

        // 2.获取用户验证后前端传过来的验证流水号等参数
        // 2.get the verification parameters passed from the front end after verification
        String lotNumber = getParams.get("lot_number");
        String captchaOutput = getParams.get("captcha_output");
        String passToken = getParams.get("pass_token");
        String genTime = getParams.get("gen_time");
        // 3.生成签名
        // 生成签名使用标准的hmac算法，使用用户当前完成验证的流水号lot_number作为原始消息message，使用客户验证私钥作为key
        // 采用sha256散列算法将message和key进行单向散列生成最终的签名
        String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);
        // 4.上传校验参数到验证服务二次验证接口, 校验用户验证状态
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("lot_number", lotNumber);
        queryParams.add("captcha_output", captchaOutput);
        queryParams.add("pass_token", passToken);
        queryParams.add("gen_time", genTime);
        queryParams.add("sign_token", signToken);
        // captcha_id 参数建议放在 url 后面, 方便请求异常时可以在日志中根据id快速定位到异常请求
        String url = String.format(domain + "/validate" + "?captcha_id=%s", captchaId);
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        JSONObject jsonObject = new JSONObject();
        //注意处理接口异常情况，当请求验证服务二次验证接口异常时做出相应异常处理
        //保证不会因为接口请求超时或服务未响应而阻碍业务流程
        String resBody="";
        try {
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(queryParams, headers);
            ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
            resBody = response.getBody();
            jsonObject = new JSONObject(resBody);
        }catch (Exception e){
            jsonObject.put("result","success");
            jsonObject.put("reason","request captcha api fail");
        }
        return jsonObject;
    }

}
