package com.zl;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadingConfig implements AsyncConfigurer {

	@Value("${default-thread-pool.core-threads}")
    private String corePoolSize;
	
	@Value("${default-thread-pool.max-threads}")
	private String maxPoolSize;
	
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	executor.setCorePoolSize(Integer.parseInt(corePoolSize));
    	executor.setMaxPoolSize(Integer.parseInt(maxPoolSize));
    	executor.setThreadNamePrefix("com.zl.spring-default-thread-pool");
    	executor.initialize();
        return executor;
	}
}
