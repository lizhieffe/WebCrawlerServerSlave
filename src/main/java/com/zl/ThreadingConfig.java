package com.zl;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.zl.utils.AppProperties;

@Configuration
@EnableAsync
public class ThreadingConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(Integer.parseInt(AppProperties.getInstance().get("default-thread-pool.core-threads")));
		executor.setMaxPoolSize(Integer.parseInt(AppProperties.getInstance().get("default-thread-pool.max-threads")));
        executor.setThreadNamePrefix("com.zl.spring-default-thread-pool");
        executor.initialize();
        return executor;
	}
}
