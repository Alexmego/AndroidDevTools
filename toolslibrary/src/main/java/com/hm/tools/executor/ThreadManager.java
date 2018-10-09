package com.hm.tools.executor;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;


public class ThreadManager {

    private static ThreadPool mThreadPool;

    private ThreadManager() {
        if (mThreadPool == null) {
            int CPU_COUNT = Runtime.getRuntime().availableProcessors();
            int corePoolSize = CPU_COUNT + 1;
            int maximumPoolSize = CPU_COUNT * 2 + 1;
            long keepAliveTime = 1;
            mThreadPool = new ThreadPool(corePoolSize, maximumPoolSize, keepAliveTime);
        }
    }

    public static void execute(Runnable runnable) {
        if (mThreadPool != null) {
            mThreadPool.executor(runnable);
        }
    }

    public static void cancel(Runnable runnable) {
        if (mThreadPool != null) {
            mThreadPool.cancel(runnable);
        }
    }

    public static ThreadManager getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static ThreadManager INSTANCE = new ThreadManager();
    }


    public static class ThreadPool {
        private static ThreadPoolExecutor executor = null;

        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime = 0; // 限制线程的的最大存活时间

        ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            super();
            this.corePoolSize = corePoolSize;  //核心线程数
            this.maximumPoolSize = maximumPoolSize; //最大线程 ，当核心线程用完时。决定是否开启最大线程
            this.keepAliveTime = keepAliveTime;  //线程排队时间
        }

        void executor(Runnable runnable) {
            if (runnable == null) {
                return;
            }

            if (executor == null || executor.isShutdown()) {
                executor = new ThreadPoolExecutor(
                        corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new DefaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
            }
            executor.execute(runnable);

        }

        void cancel(Runnable runnable) {
            if (runnable != null && !executor.isShutdown()) {
                executor.getQueue().remove(runnable);
            }
        }

        public static class DefaultThreadFactory implements ThreadFactory {
            int threadNum = 0;

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                final Thread result = new Thread(runnable, "ume-pool-thread-" + threadNum) {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                        super.run();
                    }
                };
                threadNum++;
                return result;
            }
        }

    }

}
