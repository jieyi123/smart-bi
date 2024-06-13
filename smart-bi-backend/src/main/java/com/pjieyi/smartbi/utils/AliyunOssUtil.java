package com.pjieyi.smartbi.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @Author pjieyi
 * @Description
 */
@Component
public class AliyunOssUtil {

    @Value("${aliyun.oss.accessKeySecret}")
    private   String accessKeySecret;

    @Value("${aliyun.oss.accessKeyId}")
    private  String accessKeyId;
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private  String endpoint = "http://oss-cn-chengdu.aliyuncs.com";
    // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
    // 填写Bucket名称，例如examplebucket。
    @Value("${aliyun.oss.bucketName}")
    private  String bucketName;
    // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
    //OSS中存储的位置
    private  String objectName = "BI/";
    // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt
    // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
    private  String filePath= "C:\\Users\\pjy17\\Pictures\\Screenshots\\屏幕截图 2023-06-02 111203.png";


    public  String upload(String fileName,InputStream inputStream){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        String url="";
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName+fileName, inputStream);
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);
            // 上传文件
            PutObjectResult result = ossClient.putObject(putObjectRequest);
           // url="https://https://aliyunoss.originai.icu"+"."+endpoint.substring(endpoint.lastIndexOf("/")+1)+"/"+objectName+fileName;
            url="http://aliyunoss.originai.icu"+"/"+objectName+fileName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}
