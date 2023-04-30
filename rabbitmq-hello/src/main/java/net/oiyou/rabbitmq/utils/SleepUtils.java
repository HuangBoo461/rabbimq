package net.oiyou.rabbitmq.utils;

/**
 * @author HuangBoo
 * @since 2023年04月30日 21:54
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(second * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
