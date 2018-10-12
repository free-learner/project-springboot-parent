//package com.personal.springboot.scheduled.conf;
//
//import java.util.concurrent.Executor;
//
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
//import org.springframework.context.annotation.AdviceMode;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//
///**
// * 异步并行任务
// * 
// * @Author  LiuBao
// * @Version 2.0
// *   2017年7月12日
// */
//@Configuration
//@EnableScheduling
//@EnableAsync(mode = AdviceMode.PROXY, proxyTargetClass = false, order = Ordered.HIGHEST_PRECEDENCE)
////@ComponentScan(basePackages = "com.personal.springboot")
//public class RootContextConfiguration implements AsyncConfigurer, SchedulingConfigurer {
//
//    @Bean
//    public ThreadPoolTaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(100);
//        scheduler.setThreadNamePrefix("task-");
//        scheduler.setAwaitTerminationSeconds(60);
//        scheduler.setWaitForTasksToCompleteOnShutdown(true);
//        return scheduler;
//    }
//
//    @Override
//    public Executor getAsyncExecutor() {
//        Executor executor = this.taskScheduler();
//        return executor;
//    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar registrar) {
//        TaskScheduler scheduler = this.taskScheduler();
//        registrar.setTaskScheduler(scheduler);
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new SimpleAsyncUncaughtExceptionHandler();
//    }
//
//}