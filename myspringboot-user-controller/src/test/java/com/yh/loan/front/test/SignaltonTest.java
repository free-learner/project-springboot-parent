package com.yh.loan.front.test;

/**
 * 单例变量初始化测试
 * 
 * @Author LiuBao
 * @Version 2.0 2017年7月13日
 */
class Signalton {
    private static Signalton signalton = new Signalton();
    public static int count1;
    public static int count2 = 0;

    /**
     * 执行顺序:
     * 1.静态变量默认初始化        0 0
     * 2.构造方法先执行                1 1
     * 3.静态变量赋值语句执行    1 0
     */
    public Signalton() {
        super();
        count1++;
        count2++;
    }

    public static Signalton getInstance() {
        return signalton;
    }

}

public class SignaltonTest {

    @SuppressWarnings("static-access")
    public static void main(String[] args) {
        Signalton instance = Signalton.getInstance();
        System.out.println("结果count1为:" + instance.count1);
        System.out.println("结果count2为:" + instance.count2);
    }

}
