package com.zl;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadingConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(30);
        executor.setThreadNamePrefix("com.zl.spring-default-thread-pool");
        executor.initialize();
        return executor;
	}
}
