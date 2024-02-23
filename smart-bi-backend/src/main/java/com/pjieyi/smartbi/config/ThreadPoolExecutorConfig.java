package com.pjieyi.smartbi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pjieyi
 * @desc 线程池
 */
@Configuration
public class ThreadPoolExecutorConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        //创建一个线程工厂
        ThreadFactory threadFactory=new ThreadFactory() {
            //初始化线程数1
            private int count=1;
            @Override
            public Thread newThread(Runnable r) {
                //创建一个新线程
                Thread thread=new Thread(r);
                thread.setName("线程"+count);
                count++;
                return thread;
            }
        };
        // 创建一个新的线程池，线程池核心大小为2，最大线程数为4，
        // 非核心线程空闲时间为100秒，任务队列为阻塞队列，长度为4，使用自定义的线程工厂创建线程
        //如果阻塞队列满后 新来一个线程并且当前线程没有到达最大线程数 就会直接执行新来的这个任务 不是从阻塞队列中取
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(
                2,4,100, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(4),threadFactory);

        return threadPoolExecutor;

    }
}
