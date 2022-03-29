package ru.spbe.redisexample.loader;

import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redisrefresher.RedisRefresher;

public class ExampleRefresher extends RedisRefresher<ExampleEntity> {
    public ExampleRefresher(ExampleLoader loader){
        super(loader);
    }
}