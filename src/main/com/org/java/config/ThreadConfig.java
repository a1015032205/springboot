package org.java.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @author Administrator
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //CUP核心数
        int core = Runtime.getRuntime().availableProcessors();
        log.info("CPU核心数：" + core);
        //最大核心数
        threadPoolTaskExecutor.setCorePoolSize(core);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize((core << 1) + core);
        //等待队列数量
        threadPoolTaskExecutor.setQueueCapacity(100);
        //线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("SpringRain-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncUncaughtExceptionHandler();
    }

    public static class SpringAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {

        }
    }

}
