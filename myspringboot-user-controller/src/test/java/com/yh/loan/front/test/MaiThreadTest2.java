package com.yh.loan.front.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * MaiThread测试工具类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年10月18日
 */
public class MaiThreadTest2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaiThreadTest2.class);
    
    public static void main(String[] args) throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info("子线程执行开始!");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    LOGGER.error("线程执行异常!",e);
                }
                LOGGER.info("子线程执行结束!");
//                Thread.currentThread().notifyAll();
                stopWatch.stop();
            }
        });
        LOGGER.info("主线程执行开始!");
        thread.start();
//        thread.join();
//        Thread.currentThread().wait();
        while(stopWatch.isRunning()){
            Thread.sleep(1000);
        }
        LOGGER.info("主线程执行结束!"+stopWatch.getTotalTimeSeconds());
    }

}
