package com.personal.springboot.gataway.task;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Our implementation of an ScheduledThreadPoolExecutor two executes MyScheduledTasks tasks. It extends
 * the ScheduledThreadPoolExecutor class
 *
 */
public class ThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    /**
     * Constructor of the class. Calls the constructor of its parent class using the super keyword
     * @param corePoolSize Number of threads to keep in the pool
     */
    public ThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }


 
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
    	ScheduledTask<V> myTask=new ScheduledTask<V>(runnable, null, task,this);    
        return myTask;
    }


    /**
     * Method that schedule in the executor a periodic tasks. It calls the method of its parent class using
     * the super keyword and stores the period of the task.
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> task= super.scheduleAtFixedRate(command, initialDelay, period, unit);
        ScheduledTask<?> myTask=(ScheduledTask<?>)task;
        myTask.setPeriod(TimeUnit.MILLISECONDS.convert(period,unit));
        return task;
    }

}