package com.pjieyi.smartbi.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pjieyi.smartbi.common.BaseResponse;
import com.pjieyi.smartbi.common.DeleteRequest;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.common.ResultUtils;
import com.pjieyi.smartbi.constant.FileConstant;
import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.exception.ThrowUtils;
import com.pjieyi.smartbi.model.dto.chart.ChartAddRequest;
import com.pjieyi.smartbi.model.dto.chart.ChartQueryRequest;
import com.pjieyi.smartbi.model.dto.chart.ChartUpdateRequest;
import com.pjieyi.smartbi.model.dto.chart.GenChartByAiRequest;
import com.pjieyi.smartbi.model.entity.Chart;
import com.pjieyi.smartbi.model.entity.User;
import com.pjieyi.smartbi.model.enums.FileUploadBizEnum;
import com.pjieyi.smartbi.model.file.UploadFileRequest;
import com.pjieyi.smartbi.service.ChartService;
import com.pjieyi.smartbi.service.UserService;
import com.pjieyi.smartbi.utils.AliyunOssUtil;
import com.pjieyi.smartbi.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 用户接口
 * @author pjieyi
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;
    @Resource
    private UserService userService;

    @Resource
    private AliyunOssUtil aliyunOssUtil;
    

    // region 增删改查

    /**
     * 新增图表信息
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        boolean result = chartService.save(chart);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(chart.getId());
    }

    /**
     * 删除图表信息
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = chartService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新图表信息
     * @param chartUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest, HttpServletRequest request) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取图表信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(int id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        return ResultUtils.success(chart);
    }

    /**
     * 获取图表信息列表
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Chart>> listChart(ChartQueryRequest chartQueryRequest, HttpServletRequest request) {

        Chart chartQuery = new Chart();
        if (chartQueryRequest != null) {
            BeanUtils.copyProperties(chartQueryRequest, chartQuery);
        }

        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>(chartQuery);
        List<Chart> chartList = chartService.list(queryWrapper);
        //List<ChartVO> chartVOList = chartList.stream().map(chart -> {
        //    ChartVO chartVO = new ChartVO();
        //    BeanUtils.copyProperties(chart, chartVO);
        //    return chartVO;
        //}).collect(Collectors.toList());
        return ResultUtils.success(chartList);
    }

    /**
     * 分页获取图表信息列表
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(ChartQueryRequest chartQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        Chart chartQuery = new Chart();
        //if (chartQueryRequest != null) {
        //    BeanUtils.copyProperties(chartQueryRequest, chartQuery);
        //    current = chartQueryRequest.getCurrent();
        //    size = chartQueryRequest.getPageSize();
        //}

        LambdaQueryWrapper<Chart> queryWrapper=new LambdaQueryWrapper<>();
        //String chartAccount = chartQuery.getChartAccount();
        //String chartRole = chartQuery.getChartRole();
        //String chartName = chartQuery.getChartName();
        //String phone = chartQuery.getPhone();
        //String email = chartQuery.getEmail();
        //String startTime = chartQueryRequest.getStartTime();
        //String endTime = chartQueryRequest.getEndTime();
        //Integer gender = chartQuery.getGender();
        ////模糊查询
        //if (StringUtils.isNotEmpty(chartAccount)){
        //    queryWrapper.like(Chart::getChartAccount,chartAccount);
        //}
        //if (StringUtils.isNotEmpty(chartName)){
        //    queryWrapper.like(Chart::getChartName,chartName);
        //}
        //if (StringUtils.isNotEmpty(phone)){
        //    queryWrapper.like(Chart::getPhone,phone);
        //}
        //if (StringUtils.isNotEmpty(email)){
        //    queryWrapper.like(Chart::getEmail,email);
        //}
        //if(gender != null){
        //    queryWrapper.like(Chart::getGender,gender);
        //}
        //if (StringUtils.isNotEmpty(chartRole)){
        //    queryWrapper.like(Chart::getChartRole,chartRole);
        //}
        //if (StringUtils.isNotEmpty(startTime) && StringUtils.isNoneEmpty(endTime)){
        //    //大于等于
        //    queryWrapper.ge(Chart::getCreateTime,startTime);
        //    //小于等于
        //    queryWrapper.le(Chart::getCreateTime,endTime);
        //}
        //queryWrapper.orderByDesc(Chart::getUpdateTime);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size), queryWrapper);
        //Page<ChartVO> chartVOPage = new PageDTO<>(chartPage.getCurrent(), chartPage.getSize(), chartPage.getTotal());
        //List<ChartVO> chartVOList = chartPage.getRecords().stream().map(chart -> {
        //    ChartVO chartVO = new ChartVO();
        //    BeanUtils.copyProperties(chart, chartVO);
        //    return chartVO;
        //}).collect(Collectors.toList());
        //chartVOPage.setRecords(chartVOList);
        return ResultUtils.success(chartPage);
    }

    // endregion

    /**
     * 文件上传 智能分析
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        ThrowUtils.throwIf(StringUtils.isBlank(goal),ErrorCode.PARAMS_ERROR,"目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name)&&name.length()>100,ErrorCode.PARAMS_ERROR,"名称过长");
        StringBuilder builder=new StringBuilder("你是一个数据分析师，接下来我会给你我的分析目标和原始数据，请告诉我分析结论。");
        builder.append("\n");
        builder.append("分析目标:").append(goal+"\n");
        String data = ExcelUtils.excelToCsv(multipartFile);
        builder.append("数据:\n"+data);
        System.out.println(builder.toString());
        return ResultUtils.success(builder.toString());
        //User loginUser = userService.getLoginUser(request);
        //// 文件目录：根据业务、用户来划分
        //String uuid = RandomStringUtils.randomAlphanumeric(8);
        //String filename = uuid + "-" + multipartFile.getOriginalFilename();
        //String filepath = String.format("/%s/%s/%s", "data_analysis", loginUser.getId(), filename);
        //File file = null;
        //try {
        //    // 上传文件
        //    file = File.createTempFile(filepath, null);
        //    multipartFile.transferTo(file);
        //    //aliyunOssUtil.upload(filepath,multipartFile.getInputStream());
        //    // 返回可访问地址
        //    return ResultUtils.success(ExcelUtils.excelToCsv(multipartFile));
        //} catch (Exception e) {
        //    log.error("file upload error, filepath = " + filepath, e);
        //    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        //} finally {
        //    if (file != null) {
        //        // 删除临时文件
        //        boolean delete = file.delete();
        //        if (!delete) {
        //            log.error("file delete error, filepath = {}", filepath);
        //        }
        //    }
        //}
    }

}
