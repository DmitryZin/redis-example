package ru.spbe.redisexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.spbe.redisexample.entity.ExampleEntity;
import ru.spbe.redisexample.loader.ExampleLoader;
import ru.spbe.redisexample.publisher.ExamplePublisher;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@SpringBootApplication
@Slf4j
public class RedisExampleApplication {

	public static void main(String[] args) throws JsonProcessingException, InterruptedException {
		var context = SpringApplication.run(RedisExampleApplication.class, args);
		// имитация изначального наполнения репозитория
		List<ExampleEntity> someRepositories = new ArrayList<>();
		ExampleEntity entity1 = new ExampleEntity();
		entity1.setId(1);
		entity1.setName("Test 1");
		someRepositories.add(entity1);
		ExampleEntity entity2 = new ExampleEntity();
		entity2.setId(2);
		entity2.setName("Test 2");
		someRepositories.add(entity2);

		//сам "публицист"
		ExamplePublisher publisher = (ExamplePublisher) context.getBean("examplePublisher");
		publisher.clearValues();
		publisher.addValues(someRepositories);

		//загрузчик данных
		ExampleLoader exampleLoader = (ExampleLoader) context.getBean("exampleLoader");
		//лист для данных
		List<ExampleEntity> result = new ArrayList<>(exampleLoader.getListValues());
		log.info("create result: " + result);
		//подписываемся на обновления
		exampleLoader.subscribe(result);
		// ждем, иначе публикация будет раньше чем успеем подписаться
		// чисто тестовая необходимость
		sleep(5);
		ExampleEntity entity3 = new ExampleEntity();
		entity3.setId(3);
		entity3.setName("Test 3");
		publisher.addValue(entity3);
		log.info("add 3 result: " + result);
		// ждем, иначе данные не актуальны
		sleep(3);
		log.info("add 3(after 5 ms) result: " + result);
		// проверяем что обновилось
		// к сожалению надо немного подождать
		// смотрим что лежит в редисе
		log.info("in Redis DB result: " + exampleLoader.getListValues().toString());
		// отписываемся
		exampleLoader.unSubscribe();


	}

}
