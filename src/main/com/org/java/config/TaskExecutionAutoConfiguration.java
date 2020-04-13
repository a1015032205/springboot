package org.java.config;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-04-13 14:50
 * @Description: TaskExecutionAutoConfiguration
 */

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * EnableAutoConfiguration Auto-configuration for TaskExecutor.
 *
 * @author Stephane Nicoll
 * @author Camille Vienot
 * @since 2.1.0
 */
// 仅在类 ThreadPoolTaskExecutor 存在于 classpath 时才应用
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@Configuration
// 确保前缀为 spring.task.execution 的属性配置项被加载到 bean TaskExecutionProperties 中
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class TaskExecutionAutoConfiguration {

    /**
     * Bean name of the application TaskExecutor.
     */
    public static final String APPLICATION_TASK_EXECUTOR_BEAN_NAME = "applicationTaskExecutor";

    private final TaskExecutionProperties properties;

    private final ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers;

    private final ObjectProvider<TaskDecorator> taskDecorator;

    public TaskExecutionAutoConfiguration(TaskExecutionProperties properties,
                                          ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
                                          ObjectProvider<TaskDecorator> taskDecorator) {
        this.properties = properties;
        this.taskExecutorCustomizers = taskExecutorCustomizers;
        this.taskDecorator = taskDecorator;
    }

    // 定义 bean TaskExecutorBuilder taskExecutorBuilder
    // 这是一个 TaskExecutor 构建器
    @Bean
    // 仅在该 bean 尚未被定义时才定义
    @ConditionalOnMissingBean
    public TaskExecutorBuilder taskExecutorBuilder() {
        TaskExecutionProperties.Pool pool = this.properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity());
        builder = builder.corePoolSize(pool.getCoreSize());
        builder = builder.maxPoolSize(pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        builder = builder.threadNamePrefix(this.properties.getThreadNamePrefix());
        builder = builder.customizers(this.taskExecutorCustomizers);
        builder = builder.taskDecorator(this.taskDecorator.getIfUnique());
        return builder;
    }

    // 懒惰模式定义 bean ThreadPoolTaskExecutor applicationTaskExecutor，
    // 基于容器中存在的  TaskExecutorBuilder
    @Lazy
    // 使用bean名称 : taskExecutor, applicationTaskExecutor
    @Bean(name = {APPLICATION_TASK_EXECUTOR_BEAN_NAME,
            AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME})
    // 仅在容器中不存在类型为 Executor 的 bean 时才定义
    @ConditionalOnMissingBean(Executor.class)
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }

}