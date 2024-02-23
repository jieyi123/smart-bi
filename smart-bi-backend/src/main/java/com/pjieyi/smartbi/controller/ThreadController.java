package com.pjieyi.smartbi.controller;

import cn.hutool.json.JSONUtil;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pjieyi
 * @desc 测试线程池
 */
@RestController
@RequestMapping("/thread")
@Slf4j
public class ThreadController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;


    //新建线程
    @GetMapping("/add")
    public void add(String name){
        // 使用CompletableFuture运行一个异步任务
        try {
            CompletableFuture.runAsync(()->{
                log.info("任务执行中：" + name + "，执行人：" + Thread.currentThread().getName());
                try {
                    // 让线程休眠60分钟，模拟长时间运行的任务
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },threadPoolExecutor);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"当前用户人数过多，请稍后重试");
        }
    }

    //返回线程池的状态
    @GetMapping("/get")
    public String get(){
        // 创建一个HashMap存储线程池的状态信息
        Map<String, Object> map = new HashMap<>();
        map.put("队列长度",threadPoolExecutor.getQueue().size());
        map.put("任务总数",threadPoolExecutor.getTaskCount());
        map.put("已完成任务数",threadPoolExecutor.getCompletedTaskCount());
        map.put("正在工作的线程数",threadPoolExecutor.getActiveCount());
        map.put("最大线程数",threadPoolExecutor.getLargestPoolSize());
        return JSONUtil.toJsonStr(map);
    }

}
