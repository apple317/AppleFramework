package com.apple.thread;

import android.os.AsyncTask;
import android.os.Looper;

import com.apple.utils.LogUtils;

/**
 * 任务调度器
 * @author apple
 * @version 1.0.0
 */
public class TaskScheduler {

    private static final String TAG = "TaskScheduler";

    /**
     * 以轻量级的方式执行一个异步任务
     * @param runnable 异步任务
     */
    public static void execute(Runnable runnable) {
        execute(runnable, null);
    }

    /**
     * 以轻量级的方式执行一个异步任务
     * @param backgroundRunnable 异步任务
     * @param postExecuteRunnable 回调任务
     */
    public static void execute(final Runnable backgroundRunnable, final Runnable postExecuteRunnable) {
        if (backgroundRunnable == null) {
            if (postExecuteRunnable != null) {
                postExecuteRunnable.run();
            }
            return;
        }
        // 如果不在主线程，下面的TASK不能执行，且不需要执行
        if (Looper.myLooper() != Looper.getMainLooper()) {
            backgroundRunnable.run();
            if (postExecuteRunnable != null) {
                postExecuteRunnable.run();
            }
            return;
        }
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                backgroundRunnable.run();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (postExecuteRunnable != null) {
                    postExecuteRunnable.run();
                }
            }

            @Override
            protected void onCancelled() {
                LogUtils.d(TAG, "AsyncTask onCancelled class=%s", backgroundRunnable.getClass().getName());
                if (postExecuteRunnable != null) {
                    postExecuteRunnable.run();
                }
            }
        }
        .execute();
    }

    /**
     * 执行一个Task
     * @param task Task
     */
    public static void execute(Task task) {
        task.execute();
    }

    /**
     * Task
     * @param <Input> 输入参数类型
     * @param <Output> 输出参数类型
     */
    public static abstract class Task<Input, Output> extends AsyncTask<Input, Object, Output> {

        private Input mInput;

        /**
         * 以输入参数构造一个Task，
         * @param Input 输入参数
         */
        public Task(Input Input) {
            mInput = Input;
        }

        @Override
        protected final Output doInBackground(Input... params) {

            return onDoInBackground(params[0]);
        }

        @Override
        protected final void onPostExecute(Output result) {
            onPostExecuteForeground(result);
        }

        /**
         * 执行
         */
        public void execute() {
            execute(mInput);
        }

        /**
         * 后台任务执行回调
         * @param param 输入参数
         * @return 返回结果
         */
        protected abstract Output onDoInBackground(Input param);

        /**
         * 前台任务执行回调
         * @param result 输入参数
         */
        protected abstract void onPostExecuteForeground(Output result);
    }
}
