package com.apple.thread;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author apple
 * @version 1.0.1
 */
public final class ThreadPool {
    private static final int AWAIT_TERMINATION_TIMEOUT = 10000; //ms
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup mGroup;
        private final AtomicInteger mThreadNumber = new AtomicInteger(1);
        private final String mNamePrefix;

        DefaultThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            mGroup = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            mNamePrefix = "pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(mGroup, runnable, mNamePrefix + mThreadNumber.getAndIncrement(), 0);
            thread.setDaemon(true);
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    }

    private ThreadPoolExecutor mThreadPoolExecutor;
    private String mName;

    /**
     * 构造函数
     *
     * @param name                  名称
     * @param maxThreadCount        线程池的最大线程数
     * @param keepAliveTimeInSecond 线程空闲是保持的时间(单位:秒)
     */
    public ThreadPool(final String name, int maxThreadCount, long keepAliveTimeInSecond) {

        if (maxThreadCount <= 0) {
            throw new IllegalArgumentException("maxThreadCount must be > 0 !");
        }

        mName = name;

        mThreadPoolExecutor = new ThreadPoolExecutor(maxThreadCount, maxThreadCount,
                keepAliveTimeInSecond, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory());
    }


    /**
     * 获取线程池名称
     *
     * @return 任务名
     */
    public String getName() {
        return mName;
    }

    /**
     * 添加下载任务
     *
     * @param task 任务
     */
    public synchronized void addTask(Runnable task) {
        assertState();
        assertTaskValidity(task);
        mThreadPoolExecutor.execute(task);
    }

    /**
     * 添加一个 Callable
     * @param task task
     * @param <T> type
     * @return Future
     */
    public synchronized <T> Future<T> submit(Callable<T> task) {
        assertState();
        assertTaskValidity(task);
        return mThreadPoolExecutor.submit(task);
    }

    /**
     * 添加一个 Runnable
     * @param task task
     * @return Future
     */
    public synchronized Future<?> submit(Runnable task) {
        assertState();
        assertTaskValidity(task);
        return mThreadPoolExecutor.submit(task);
    }

    private void assertState() {
        if (mThreadPoolExecutor.isShutdown()) {
            throw new IllegalStateException("this ThreadPool has been shutdown!!");
        }
    }

    private void assertTaskValidity(Object task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null !!");
        }
    }

    /**
     * 删除下载任务
     *
     * @param task 任务
     * @return true 表示在任务队列中找到了此任务
     */
    public synchronized boolean removeTask(Runnable task) {
        mThreadPoolExecutor.purge();
        return mThreadPoolExecutor.remove(task);
    }

    /**
     * 关闭并取消在等待中的任务，尝试关闭正在执行的任务，无法关闭的等待完成，然后结束结束线程池
     *
     * @return 被取消的task
     * @throws InterruptedException InterruptedException
     */
    public synchronized List<Runnable> shutDownNowAndTermination() throws InterruptedException {
        List<Runnable> canceledTask = mThreadPoolExecutor.shutdownNow();
        mThreadPoolExecutor.awaitTermination(AWAIT_TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
        return canceledTask;
    }

    /**
     * 关闭并等待任务结束，然后关闭结束线程池
     *
     * @throws InterruptedException InterruptedException
     */
    public synchronized void shutDownAndTermination() throws InterruptedException {
        mThreadPoolExecutor.shutdown();
        mThreadPoolExecutor.awaitTermination(AWAIT_TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 是否关闭，关闭后不能加入新的任务
     *
     * @return true 表示关闭
     */
    public boolean isShutdown() {
        return mThreadPoolExecutor.isShutdown();
    }

    /**
     * 是否已经结束
     *
     * @return true 表示结束
     */
    public boolean isTerminated() {
        return mThreadPoolExecutor.isTerminated();
    }
}
