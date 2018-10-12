package com.yh.loan.front.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTest.class);

    public static int tmpNum=0;
    
    public static void main(String[] args) {
        LOGGER.info("主线程执行开始...");

//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
        ExecutorService poolExecutor = Executors.newCachedThreadPool();
        try {
            poolExecutor.execute(new ThreadTest().new MyThread());
            poolExecutor.execute(new ThreadTest().new MyThread());
            poolExecutor.submit(new MyThread2());
        } catch (Exception e) {
            LOGGER.error("主线程执行异常"+tmpNum);
        }finally{
            poolExecutor.shutdown();
        }
        
//        try {
//            new Thread(new ThreadTest().new MyThread()).start();
//            new Thread(new ThreadTest().new MyThread()).start();
//            new Thread(new MyThread2()).start();
//        } catch (Exception e) {
//            LOGGER.error("主线程执行异常"+tmpNum);
//        }
        
        LOGGER.info("主线程执行结束..."+tmpNum);
    }

    class MyThread implements Runnable /* extends Thread */ {

        @Override
        public void run() {
            LOGGER.info(String.format("子线程名:%s,其中tmpNum值为:"+tmpNum, Thread.currentThread().getName()));
            tmpNum+=10;
            tmpNum/=0;
        }

    }

}

class MyThread2 implements Runnable /* extends Thread */ {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyThread2.class);

    @Override
    public void run() {
        LOGGER.info(String.format("子线程名:%s", Thread.currentThread().getName()));
    }

}