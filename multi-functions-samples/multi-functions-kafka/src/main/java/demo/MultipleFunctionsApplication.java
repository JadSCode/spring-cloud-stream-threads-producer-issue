/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class MultipleFunctionsApplication {

	private final static String DATASOURCE_OUT = "datasource-out";

	public static void main(String[] args) {
		SpringApplication.run(MultipleFunctionsApplication.class, args);
	}

	@Autowired
	StreamBridge streamBridge;


	@Scheduled(fixedRate=10000)
	public void generateEvents() {
		System.out.println("******************");
		int type = (int) (Math.random() * 90000 % 2);
		String query = getRandomItem(Arrays.asList("q1", "q2", "q3", "q4"));
		MyMessage msg = MyMessage.builder().type(type).query(query).build();
		System.out.println("Sending message " + msg);
		streamBridge.send("schedules", msg);
	}

	@Bean
	public Consumer<MyMessage> consumerSearch1() {
		return message -> {
			System.out.println("******************");
			System.out.println("consumerSearch1");
			System.out.println("******************");
			System.out.println("Received message " + message);
			MyMessageResult result = MyMessageResult.builder().origin("search1").query(message.getQuery()).results("search 1 result").build();
			streamBridge.send(DATASOURCE_OUT, result);
		};
	}

	@Bean
	public Consumer<MyMessage> consumerSearch2() {
		return message -> {
			System.out.println("******************");
			System.out.println("consumerSearch2");
			System.out.println("******************");
			System.out.println("Received message " + message);
			MyMessageResult result = MyMessageResult.builder().origin("search2").query(message.getQuery()).results("search 2 result").build();
			streamBridge.send(DATASOURCE_OUT, result);
		};
	}


	@Bean
	public KafkaHeaderMapper kafkaHeaderMapper() {
		return new DefaultKafkaHeaderMapper();
	}


	public static String getRandomItem(List<String> items) {
		return items.get(new Random().nextInt(items.size()));
	}



}
