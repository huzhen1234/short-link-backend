package com.hutu.shortlinkcommon.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ThreadPoolConfig {

    @Bean("threadExecutor")
    public ThreadPoolTaskExecutor threadExecutor() {
        // spring所管理的,赋予了优雅停机 ExecutorConfigurationSupport -> DisposableBean
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 优雅停机 当spring关闭时,调用destroy时会调用shutdown方法
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("shot-link-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("shot-link-executor-" + counter.incrementAndGet());
                thread.setUncaughtExceptionHandler((t, e) -> {
                    LoggerFactory.getLogger("ThreadPoolError").error(
                            "线程[{}]执行异常: {}", t.getName(), e.getMessage(), e
                    );
                });
                return thread;
            }
        });
        executor.initialize();
        return executor;
    }
}
