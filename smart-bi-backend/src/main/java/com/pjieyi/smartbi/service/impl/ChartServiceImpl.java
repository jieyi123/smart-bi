package com.pjieyi.smartbi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.exception.ThrowUtils;
import com.pjieyi.smartbi.manager.AiManager;
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

    /**
     * AI生成数据
     *
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
        String data = ExcelUtils.excelToCsv(multipartFile);
        builder.append("原始数据:\n"+data);
        String requestData = builder.toString();
        //modelId为ai的预设模型
        String responseData = aiManager.doChart(requestData, 1756296792985034754L);
        //预设模型以【【【【【作为分隔
        String[] splits = responseData.split("【【【【【");
        //只有三部分
        if (splits.length<3){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI生成错误");
        }
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
}




