package ru.spbe.redisexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redisloader.RedisLoader;
import ru.spbe.redispublisher.RedisPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@SpringBootApplication
@Slf4j
public class RedisExampleApplication {

	public static void main(String[] args) throws JsonProcessingException, InterruptedException {
		var context = SpringApplication.run(RedisExampleApplication.class, args);
		// имитация изначального наполнения репозитория
		Map<Integer, ExampleEntity> someRepositories = new HashMap<>();
		ExampleEntity entity1 = new ExampleEntity();
		entity1.setId(1);
		entity1.setName("Test 1");
		someRepositories.put(entity1.getId(), entity1);
		ExampleEntity entity2 = new ExampleEntity();
		entity2.setId(2);
		entity2.setName("Test 2");
		someRepositories.put(entity2.getId(), entity2);

		//сам "публицист"
		RedisPublisher<Integer, ExampleEntity> publisher = (RedisPublisher<Integer, ExampleEntity>) context.getBean("examplePublisher");
		publisher.clearValues();
		publisher.publish(someRepositories);
		RedisLoader<Integer, ExampleEntity> exampleLoader = (RedisLoader<Integer, ExampleEntity>) context.getBean("exampleLoader");
		log.info("in Redis DB result: " + exampleLoader.getValues().toString());
		sleep(5);
		log.info("in Redis DB result: " + exampleLoader.getValues().toString());

		ExampleEntity entity3 = new ExampleEntity();
		entity3.setId(3);
		entity3.setName("Test 3");
		publisher.publish(entity3.getId(), entity3);
		exampleLoader.reLoad();
		//загрузчик данных

		//лист для данных
		var result = exampleLoader.getValues();
		log.info("add 3 result: " + result);
		log.info("in Redis DB result: " + exampleLoader.getValues().toString());
		// отписываемся
		sleep(50);
		exampleLoader.unSubscribe(); // чтоб завершить поток и выйти из программы
	}

}
