package ru.spbe.redisexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redisexample.loader.ExampleLoader;
import ru.spbe.redisexample.loader.ExampleRefresher;
import ru.spbe.redisexample.publisher.ExamplePublisher;
import ru.spbe.redisstarter.RedisListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class RedisExampleApplication {

	public static void main(String[] args) throws JsonProcessingException, InterruptedException {
		var context = SpringApplication.run(RedisExampleApplication.class, args);
		List<ExampleEntity> someRepositories = new ArrayList<>();
		ExampleEntity entity1 = new ExampleEntity();
		entity1.setId(1);
		entity1.setName("Test 1");
		someRepositories.add(entity1);
		ExampleEntity entity2 = new ExampleEntity();
		entity2.setId(2);
		entity2.setName("Test 2");
		someRepositories.add(entity2);

		ExamplePublisher publisher = (ExamplePublisher) context.getBean("examplePublisher");
		publisher.clearValues();
		publisher.addValues(someRepositories);

		ExampleLoader exampleLoader = (ExampleLoader) context.getBean("exampleLoader");
		//первоначальная загрузка данных
		List<ExampleEntity> result = new ArrayList<>(exampleLoader.getListValues());
		ExampleRefresher exampleRefresher = (ExampleRefresher) context.getBean("exampleRefresher");
		//говорим, что его надо обновлять
		exampleRefresher.setList(result);
		RedisListener exampleRedisListener = (RedisListener) context.getBean("exampleRedisListener");


		ExampleEntity entity3 = new ExampleEntity();
		entity3.setId(3);
		entity3.setName("Test 3");
		publisher.addValue(entity3);


		sleep(1000);
		// проверяем что обновилось
		System.out.println(result);
		// смотрим что лежит в редисе
		result = exampleLoader.getListValues();
		System.out.println(result);
		// отписываемся
		exampleRedisListener.unSubscribe();



	}

}
