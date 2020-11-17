package com.shiki.demo.fun;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @Author shiki
 * @description: 函数式工具类
 * @Date 2020/11/17 下午3:35
 */
public interface Fun {
    /**
     * 输出到指定目录
     *
     * @param file:     文件名
     * @param consumer: 处理方法
     * @Author: shiki
     * @Date: 2020/11/17 下午3:38
     */
    static <T> void out(String file, Consumer<PrintStream> consumer) {
        try (final PrintStream modify = new PrintStream(new File(file))) {
            consumer.accept(modify);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
