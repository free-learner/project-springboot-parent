package com.yh.loan.front.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MaiThread测试工具类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年10月18日
 */
public class MaiThreadTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaiThreadTest.class);
    
    private static Semaphore semaphore = new Semaphore(1);// 设置并发信号量为5  
    // 这句代码详细注释见主函数，另外我这里为了方便直接设置参数为100，实际开发可以根据需求实例化  
    private static CountDownLatch countDownLatch = new CountDownLatch(1);  

    public static void main(String[] args) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info("子线程执行开始!");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    LOGGER.error("线程执行异常!",e);
                }
                countDownLatch.countDown();
//                semaphore.release();// 释放一把锁，必须释放，必须是方法最后一步
                LOGGER.info("子线程执行结束!");
//                Thread.currentThread().notifyAll();
            }
        });
        LOGGER.info("主线程执行开始!");
//        semaphore.acquire();// 获取一把锁，因为是在匿名内部类中使用，所以需要将其声明为成员变量 
        thread.start();
        countDownLatch.await();
//        thread.join();
//        Thread.currentThread().wait();
        LOGGER.info("主线程执行结束!");
    }

}
