package com.shiki.demo.constants;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 需要使用到的常量
 *
 * @Author: shiki
 * @Date: 2020/10/27 上午10:52
 */
public interface BaseConstants {

    /**
     * 接口初始化
     *
     * @Author: shiki
     * @Date: 2020/11/20 上午11:16
     */
    boolean isInit = new Object() {
        /**
         * 标记初始化
         * @Author: shiki
         * @Date: 2020/11/20 上午11:24
         */
        boolean isInit = true;
        {
            System.out.println("BaseConstants init start");
            final File root_path = new File(ROOT_PATH);
            if (!root_path.exists()) {
                isInit = root_path.mkdir();
            }
            final File old_clear = new File(OLD_CLEAR);
            if (!old_clear.exists()) {
                isInit = isInit && old_clear.mkdir();
            }
            final File clear_dev = new File(CLEAR_DEV);
            if (!clear_dev.exists()) {
                isInit = isInit && clear_dev.mkdir();
            }
        }
    }.isInit;

    /**
     * 文件输出位置
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午5:40
     * @see #ROOT_PATH 输出根目录
     */
    String ROOT_PATH = "/home/shiki/code/output/";
    String OLD_CLEAR = ROOT_PATH + "old_clear/";
    String CLEAR_DEV = ROOT_PATH + "clear_dev/";

    int C = Runtime.getRuntime().availableProcessors();
    int RUN = C < 8 ? C : C - 4;
    /**
     * 创建线程池
     *
     * @Author: shiki
     * @Date: 2020/10/28 上午10:35
     */
    ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            RUN,
            2 * RUN,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4 * RUN), run -> new Thread(new ThreadGroup("shiki"), run, "sl"));
}