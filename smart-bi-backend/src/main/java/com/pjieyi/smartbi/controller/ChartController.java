package com.pjieyi.smartbi.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pjieyi.smartbi.annotation.AuthCheck;
import com.pjieyi.smartbi.common.BaseResponse;
import com.pjieyi.smartbi.common.DeleteRequest;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.common.ResultUtils;
import com.pjieyi.smartbi.constant.FileConstant;
import com.pjieyi.smartbi.constant.UserConstant;
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
import com.pjieyi.smartbi.model.vo.BiResponse;
import com.pjieyi.smartbi.service.ChartService;
import com.pjieyi.smartbi.service.UserService;
import com.pjieyi.smartbi.utils.AliyunOssUtil;
import com.pjieyi.smartbi.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.util.CollectionUtils;
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
@Profile({"dev","test"}) //只允许dev和test环境可以使用
public class ChartController {

    @Resource
    private ChartService chartService;
    @Resource
    private UserService userService;

    

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
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
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
    @PostMapping("/list")
    public BaseResponse<List<Chart>> listChart(@RequestBody ChartQueryRequest chartQueryRequest, HttpServletRequest request) {

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
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest, HttpServletRequest request) {
        String name = chartQueryRequest.getName();
        LambdaQueryWrapper<Chart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Chart::getName,name);
        queryWrapper.orderByDesc(Chart::getUpdateTime);
        Page<Chart> chartPage = chartService.page(new Page<>(chartQueryRequest.getCurrent(), chartQueryRequest.getPageSize()), queryWrapper);
        return ResultUtils.success(chartPage);
    }

    // endregion

    /**
     * 智能分析
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> getChart(@RequestPart("file") MultipartFile multipartFile,
                                           GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {

        if (multipartFile.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (genChartByAiRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        BiResponse result=chartService.genChart(multipartFile,genChartByAiRequest,loginUser);
        return ResultUtils.success(result);
    }

}
