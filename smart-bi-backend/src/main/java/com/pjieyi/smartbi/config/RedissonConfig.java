package com.pjieyi.smartbi.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pjieyi
 * @desc
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private Integer database;
    private String host;
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        //1.创建配置对象
        Config config=new Config();
        String address=String.format("redis://%s:%s",host,port);
        //添加单机Redisson配置
        config.useSingleServer()
                //.setPassword("pjieyi")
                .setAddress(address)
                .setDatabase(1);
        return Redisson.create(config);
    }
}
