package ru.spbe.redisexample.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import ru.spbe.redisexample.loader.ExampleLoader;
import ru.spbe.redisexample.loader.ExampleRefresher;
import ru.spbe.redisexample.publisher.ExamplePublisher;
import ru.spbe.redisstarter.Redis;
import ru.spbe.redisstarter.RedisListener;
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
    public ExamplePublisher examplePublisher(RedisSet exampleRedisSet,
                                             Redis redis,
                                             ObjectMapper mapper){
        return new ExamplePublisher(redis, exampleRedisSet, mapper);
    }
//////////// Для чтения данных
    @Bean
    public ExampleLoader exampleLoader(RedisSet exampleRedisSet,
                                       Redis redis,
                                       ObjectMapper mapper){
        return new ExampleLoader(redis, exampleRedisSet, mapper);
    }

    @Bean
    public ExampleRefresher exampleRefresher(ExampleLoader exampleLoader){
        return new ExampleRefresher(exampleLoader);
    }

    @Bean
    public RedisListener exampleRedisListener(Redis redis, ExampleRefresher exampleRefresher, RedisSet exampleRedisSet){
        RedisListener listener = new RedisListener(redis, exampleRefresher, exampleRedisSet);
        listener.subscribe();
        return listener;
    }
}
