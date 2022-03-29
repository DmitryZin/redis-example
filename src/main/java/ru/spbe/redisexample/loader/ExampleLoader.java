package ru.spbe.redisexample.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redisloader.RedisLoader;
import ru.spbe.redisstarter.Redis;
import ru.spbe.redisstarter.RedisSet;

public class ExampleLoader extends RedisLoader<ExampleEntity> {
    public ExampleLoader(Redis redis, RedisSet set, ObjectMapper mapper) {
        super(ExampleEntity.class, redis, set, mapper);
    }
}
