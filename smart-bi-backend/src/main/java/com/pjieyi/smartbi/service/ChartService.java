package com.pjieyi.smartbi.service;

import com.pjieyi.smartbi.model.dto.chart.GenChartByAiRequest;
import com.pjieyi.smartbi.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pjieyi.smartbi.model.entity.User;
import com.pjieyi.smartbi.model.vo.BiResponse;
import org.springframework.web.multipart.MultipartFile;

/**
* @author pjy17
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-02-21 18:38:15
*/
public interface ChartService extends IService<Chart> {

    /**
     * 利用AI生成数据
     * @param multipartFile
     * @param genChartByAiRequest
     * @param loginUser
     * @return
     */
    BiResponse genChart(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser);

    /**
     * 利用AI生成数据 异步调用 线程池
     * @param multipartFile
     * @param genChartByAiRequest
     * @param loginUser
     * @return
     */
    BiResponse genChartAsync(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser);


    /**
     * 利用AI生成数据 异步调用 消息队列
     * @param multipartFile
     * @param genChartByAiRequest
     * @param loginUser
     * @return
     */
    BiResponse genChartAsyncMq(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser);


    /**
     * 转发压缩后的csv格式数据
     * @param data
     * @return
     */
    String getData(String data);
}
