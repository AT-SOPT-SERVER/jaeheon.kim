package org.sopt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {
	public static final String I_O_TASK_THREAD_POOL_NAME = "IOTaskExecutor";

	@Bean(name = I_O_TASK_THREAD_POOL_NAME)
	public ThreadPoolTaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(30);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(50);
		executor.initialize();
		return executor;
	}
}
