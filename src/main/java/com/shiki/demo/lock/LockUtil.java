package com.shiki.demo.lock;

import io.vavr.control.Either;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * @Author shiki
 * @description: 锁简化
 * @Date 2020/12/1 上午10:49
 */
public interface LockUtil {

    static <T> Either<T, Exception> lock(ReentrantLock lock, Supplier<T> supplier) {
        lock.lock();
        try {
            return Either.left(supplier.get());
        } catch (Exception e) {
            return Either.right(e);
        } finally {
            lock.unlock();
        }
    }


    static <T> Either<T, Exception> readLock(ReentrantLock lock, Supplier<T> supplier) {
        lock.lock();
        try {
            return Either.left(supplier.get());
        } catch (Exception e) {
            return Either.right(e);
        } finally {
            lock.unlock();
        }
    }
}
