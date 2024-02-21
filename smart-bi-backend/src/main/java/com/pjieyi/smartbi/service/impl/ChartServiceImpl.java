package com.pjieyi.smartbi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pjieyi.smartbi.model.entity.Chart;
import com.pjieyi.smartbi.service.ChartService;
import com.pjieyi.smartbi.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author pjy17
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-02-21 18:38:15
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




