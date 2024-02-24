package com.pjieyi.smartbi.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.exception.ThrowUtils;
import com.pjieyi.smartbi.manager.AiManager;
import com.pjieyi.smartbi.manager.RedisLimiterManager;
import com.pjieyi.smartbi.model.dto.chart.GenChartByAiRequest;
import com.pjieyi.smartbi.model.entity.Chart;
import com.pjieyi.smartbi.model.entity.User;
import com.pjieyi.smartbi.model.vo.BiResponse;
import com.pjieyi.smartbi.service.ChartService;
import com.pjieyi.smartbi.mapper.ChartMapper;
import com.pjieyi.smartbi.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
* @author pjy17
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-02-21 18:38:15
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{


    @Resource
    private AiManager aiManager;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * AI生成数据 同步调用
     * 用户的输入(参考)
     * 分析需求：分析网站用户的增长情况
     * 请使用：(图表类型)
     * 原始数据：
     * 日期,用户数
     * 1号,10
     * 2号,20
     * 3号,30
     *
     */
    @Override
    public BiResponse genChart(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser) {
        if (loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Chart chart=new Chart();
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR,"目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name)&&name.length()>100,ErrorCode.PARAMS_ERROR,"名称过长");
        StringBuilder builder=new StringBuilder();
        builder.append("分析需求:").append(goal+"\n");
        if (StringUtils.isNotEmpty(chartType)){
            builder.append("请使用:"+chartType+"\n");
            chart.setChartType(chartType);
        }
        //文件校验
        validFile(multipartFile);
        //解析文件
        String data = ExcelUtils.excelToCsv(multipartFile);
        builder.append("原始数据:\n"+data);
        String requestData = builder.toString();
        //每个不同的业务进行不同的限流
        redisLimiterManager.doRateLimit("genChartAi_"+loginUser.getId());
        //modelId为ai的预设模型
        //调用AI接口
        String responseData = aiManager.doChart(requestData, 1756296792985034754L);
        //预设模型以【【【【【作为分隔
        String[] splits = responseData.split("【【【【【");
        //只有三部分
        if (splits.length<3){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI生成错误");
        }
        //返回数据拼接
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(data);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        BiResponse biResponse=new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chart.getId());
        return biResponse;
    }

    /**
     * AI生成数据 异步调用
     * 用户的输入(参考)
     * 分析需求：分析网站用户的增长情况
     * 请使用：(图表类型)
     * 原始数据：
     * 日期,用户数
     * 1号,10
     * 2号,20
     * 3号,30
     *
     */
    @Override
    public BiResponse genChartAsync(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser) {
        //每个不同的业务进行不同的限流
        redisLimiterManager.doRateLimit("genChartAi_"+loginUser.getId());
        if (loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Chart chart=new Chart();
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR,"目标为空");
        ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR,"名称为空");
        ThrowUtils.throwIf(name.length()>100,ErrorCode.PARAMS_ERROR,"名称过长");

        //字符串拼接
        StringBuilder builder=new StringBuilder();
        builder.append("分析需求:").append(goal+"\n");
        if (StringUtils.isNotEmpty(chartType)){
            builder.append("请使用:"+chartType+"\n");
            chart.setChartType(chartType);
        }
        //文件校验
        validFile(multipartFile);
        //解析文件
        String data = ExcelUtils.excelToCsv(multipartFile);

        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(data);
        chart.setUserId(loginUser.getId());
        //todo 生成图表状态改为枚举
        chart.setStatus("wait");
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        builder.append("原始数据:\n"+data);
        String requestData = builder.toString();

        // 在最终的返回结果前提交一个任务
        // 建议处理任务队列满了后,抛异常的情况(因为提交任务报错了,前端会返回异常)
        Long chartId = chart.getId();
        try {
            CompletableFuture.runAsync(()->{
                //先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；
                //执行失败后，状态修改为 “失败”，记录任务失败信息。(为了防止同一个任务被多次执行)
                Chart runingChart=new Chart();
                runingChart.setId(chartId);
                runingChart.setStatus("running");
                boolean res = this.updateById(runingChart);
                if (!res){//执行失败意味着数据库出问题
                    //状态改为失败
                    handleChartUpdateError(chartId,"更新图表失败");
                }
                //调用AI接口
                String responseData = aiManager.doChart(requestData, 1756296792985034754L);
                //预设模型以【【【【【作为分隔
                String[] splits = responseData.split("【【【【【");
                //只有三部分
                if (splits.length<3){
                    handleChartUpdateError(chartId,"AI生成错误");
                }
                //返回数据拼接
                String genChart = splits[1].trim();
                String genResult = splits[2].trim();
                // 调用AI得到结果之后,再更新一次
                Chart succeedChart=new Chart();
                succeedChart.setId(chartId);
                succeedChart.setStatus("succeed");
                succeedChart.setGenChart(genChart);
                succeedChart.setGenResult(genResult);
                boolean result = this.updateById(succeedChart);
                if (!result){//插入数据失败
                    //状态改为失败
                    handleChartUpdateError(chartId,"更新图表失败");
                }
            },threadPoolExecutor);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"当前用户人数过多，请稍后重试");
        }
        BiResponse biResponse=new BiResponse();
        biResponse.setChartId(chartId);
        return biResponse;
    }

    // 上面的接口很多用到异常,直接定义一个工具类
    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = this.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

    /**
     * 校验文件
     * @param multipartFile
     */
    private void validFile(MultipartFile multipartFile) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 直接获取文件后缀  hutool工具
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long TEN_M = 10 * 1024 * 1024L;
            if (fileSize > TEN_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 10M");
            }
            if (!Arrays.asList("xlsx", "xls").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }

    }
}




