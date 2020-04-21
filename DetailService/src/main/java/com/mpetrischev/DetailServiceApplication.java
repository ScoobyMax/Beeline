/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mpetrischev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@Configuration
@EnableCaching
@SpringBootApplication
public class DetailServiceApplication {

	@Value("${connection.timeout}")
	private int timeout;

	public static void main(String[] args) {
		SpringApplication.run(DetailServiceApplication.class);
	}


	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(timeout);
		((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(timeout);

		return restTemplate;
	}

	@Bean("threadPoolTaskExecutor")
	public Executor getAsyncExecutor() {
		return new ForkJoinPool(
				10, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
	}

}
