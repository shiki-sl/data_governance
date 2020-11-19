package com.shiki.demo.constants;

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
}