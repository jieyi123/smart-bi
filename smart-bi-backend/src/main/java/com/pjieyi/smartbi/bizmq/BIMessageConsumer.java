package com.pjieyi.smartbi.bizmq;

import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.exception.BusinessException;
import com.pjieyi.smartbi.manager.AiManager;
import com.pjieyi.smartbi.model.entity.Chart;
import com.pjieyi.smartbi.service.ChartService;
import com.pjieyi.smartbi.utils.ExcelUtils;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.pjieyi.smartbi.constant.BiMqConstant.BI_EXCHANGE_NAME;
import static com.pjieyi.smartbi.constant.BiMqConstant.BI_QUEUE_NAME;
import static com.pjieyi.smartbi.constant.CommonConstant.BI_MODEL_ID;

/**
 * @author pjieyi
 * @desc 息队列发送信息消费者
 */
@Component
@Slf4j
public class BIMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;


    // 使用@SneakyThrows注解简化异常处理
    @SneakyThrows
    // 使用@RabbitListener注解指定要监听的队列名称为"code_queue"，并设置消息的确认机制为手动确认
    @RabbitListener(queues = {BI_QUEUE_NAME}, ackMode = "MANUAL")
    // @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag是一个方法参数注解,用于从消息头中获取投递标签(deliveryTag),
    // 在RabbitMQ中,每条消息都会被分配一个唯一的投递标签，用于标识该消息在通道中的投递状态和顺序。通过使用@Header(AmqpHeaders.DELIVERY_TAG)注解,可以从消息头中提取出该投递标签,并将其赋值给long deliveryTag参数。
    public void receiveMessage(String message, Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        // 使用日志记录器打印接收到的消息内容
        log.info("receiveMessage message = {}", message);
        if (StringUtils.isEmpty(message)){
            // 拒绝当前消息
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        Long chartId=Long.valueOf(message);
        Chart chart = chartService.getById(chartId);
        if (chart==null){
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"图表数据为空");
        }
        Chart runingChart = new Chart();
        runingChart.setId(chartId);
        runingChart.setStatus("running");
        boolean res = chartService.updateById(runingChart);
        if (!res) {//执行失败意味着数据库出问题
            //状态改为失败
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chartId, "更新图表失败");
        }
        //调用AI接口
        String responseData = aiManager.doChart(buildUserInput(chart), BI_MODEL_ID);
        if (StringUtils.isEmpty(responseData)){
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chartId, "AI生成错误");
            return;
        }
        //预设模型以【【【【【作为分隔
        String[] splits = responseData.split("【【【【【");
        //只有三部分
        if (splits.length < 3) {
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chartId, "AI生成错误");
            return;
        }
        //返回数据拼接
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        // 调用AI得到结果之后,再更新一次
        Chart succeedChart = new Chart();
        succeedChart.setId(chartId);
        succeedChart.setStatus("succeed");
        succeedChart.setGenChart(genChart);
        succeedChart.setGenResult(genResult);
        boolean result = chartService.updateById(succeedChart);
        if (!result) {//插入数据失败
            //状态改为失败
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chartId, "图表保存失败");
        }
        // 手动确认消息的接收，向RabbitMQ发送确认消息
        channel.basicAck(deliveryTag, false);
    }


    //字符串拼接
    public String buildUserInput(Chart chart){
        String chartType=chart.getChartType();
        StringBuilder builder = new StringBuilder();
        builder.append("分析需求:").append(chart.getGoal() + "\n");
        if (StringUtils.isNotEmpty(chartType)) {
            builder.append("请使用:" + chartType + "\n");
            chart.setChartType(chartType);
        }

        //解析文件
        String data = chart.getChartData();
        builder.append("原始数据:\n" + data);
       return builder.toString();
    }

    //错误处理
    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }
}
