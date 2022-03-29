package ru.spbe.redisexample.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redispublisher.RedisPublisher;
import ru.spbe.redisstarter.Redis;
import ru.spbe.redisstarter.RedisSet;

public class ExamplePublisher extends RedisPublisher<ExampleEntity> {
    public ExamplePublisher(Redis redis, RedisSet exampleRedisSet, ObjectMapper mapper) {
        super(redis, exampleRedisSet, mapper);
    }
}
