package com.pjieyi.smartbi.controller;

import cn.hutool.core.io.FileUtil;
import com.pjieyi.smartbi.common.BaseResponse;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.common.ResultUtils;
import com.pjieyi.smartbi.constant.FileConstant;
import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.model.entity.User;
import com.pjieyi.smartbi.model.enums.FileUploadBizEnum;
import com.pjieyi.smartbi.model.file.UploadFileRequest;
import com.pjieyi.smartbi.service.UserService;
import com.pjieyi.smartbi.utils.AliyunOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Author pjieyi
 * @Description 文件上传
 */
@RestController
@Slf4j
public class FileUploadController {

    @Resource
    private AliyunOssUtil aliyunOssUtil;

    @Resource
    private UserService userService;


    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file) throws IOException {
        //获取原始文件名
        String filename=file.getOriginalFilename();
        filename= UUID.randomUUID()+filename.substring(filename.lastIndexOf("."));
        String url=aliyunOssUtil.upload(filename,file.getInputStream());
        //file.transferTo(new File("C:\\Users\\pjy17\\Desktop\\img\\"+filename));
        return ResultUtils.success(url);
    }

    /**
     * 文件上传
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/uploadFile")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        //File file = null;
        try {
            // 上传文件
            //file = File.createTempFile(filepath, null);
            //multipartFile.transferTo(file);
            //cosManager.putObject(filepath, file);
            aliyunOssUtil.upload(filepath,multipartFile.getInputStream());
            // 返回可访问地址
            return ResultUtils.success(FileConstant.COS_HOST +filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            //if (file != null) {
            //    // 删除临时文件
            //    boolean delete = file.delete();
            //    if (!delete) {
            //        log.error("file delete error, filepath = {}", filepath);
            //    }
            //}
        }
    }



    /**
     * 校验文件
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
