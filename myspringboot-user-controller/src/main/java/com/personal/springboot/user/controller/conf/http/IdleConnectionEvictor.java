package com.personal.springboot.user.controller.conf.http;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 清理一些无用的链接
 * 
 */
public class IdleConnectionEvictor extends Thread {
    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
        this.start();// 启动线程
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}