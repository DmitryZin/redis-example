package ru.spbe.redisexample.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redisloader.RedisLoader;
import ru.spbe.redispublisher.RedisPublisher;
import ru.spbe.redisstarter.Redis;
import ru.spbe.redisstarter.RedisSet;

@Configuration
@EnableAutoConfiguration
public class CommonConfiguration {
//////////// Общее
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public RedisSet exampleRedisSet(
            @Value("${example_entity.datasetName}") final String dataSetName,
            @Value("${example_entity.channel}") final String channelName){
        return new RedisSet(dataSetName, channelName);
    }

    @Bean
    public Redis redis(
            @Value("${redis.host}") final String host,
            @Value("${redis.port}") final int port){
        return new Redis(host, port);
    }
    //////////// Для публикации

    @Bean
    public RedisPublisher<ExampleEntity> examplePublisher(RedisSet exampleRedisSet,
                                                   Redis redis,
                                                   ObjectMapper mapper){
        return new  RedisPublisher<>(redis, exampleRedisSet, mapper);
    }
//////////// Для чтения данных
    @Bean
    public RedisLoader<ExampleEntity> exampleLoader(RedisSet exampleRedisSet,
                                                    Redis redis,
                                                    ObjectMapper mapper){
        return new RedisLoader<>(ExampleEntity.class, redis, exampleRedisSet, mapper);
    }
}
