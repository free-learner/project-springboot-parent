package com.personal.springboot.gataway.task;

import java.util.concurrent.FutureTask;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.exception.OpenApiException;

@Component
public class ThreadPoolHandler {
	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolHandler.class);
	
	
	@Inject
	ThreadPoolTaskExecutor taskExecutor;
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	
	public Object addTask(AbstractTask task) {		
		try {
			FutureTask<Object> tsFutre = new FutureTask<Object>(task);
			taskExecutor.execute(tsFutre);	
			return tsFutre.get();
		} catch (TaskRejectedException e) {
			logger.error("the queue reached max deepth",e);
			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,e);
		}catch(Throwable e){
			Throwable throwable = e.getCause();
			if(throwable instanceof OpenApiException){
				throw (OpenApiException)throwable;
			}
			logger.error("exception happend on executing task with "+e.getMessage());			
			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,throwable);
		}
	}
	
	public boolean isPoolBusy(){
		int activeThreads = this.taskExecutor.getActiveCount();
		int availableThreads = this.taskExecutor.getMaxPoolSize()-activeThreads;
		return availableThreads < 1;
	}
}
