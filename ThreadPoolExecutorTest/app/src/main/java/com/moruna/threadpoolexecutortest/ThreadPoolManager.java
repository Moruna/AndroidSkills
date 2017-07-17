package com.moruna.threadpoolexecutortest;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: Moruna
 * Date: 2017-07-14
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class ThreadPoolManager {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;//核心线程数
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;//最大线程数

    private ThreadPoolExecutor threadPool;
    private static ThreadPoolManager instance;
    private Map<String, List<WeakReference<Future<?>>>> taskMap;

    public ThreadPoolManager() {
        threadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        taskMap = new WeakHashMap<>();
    }

    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    /**
     * 延时多久开始执行任务
     *
     * @param runnable
     * @param tag
     * @param delay
     * @param unit
     */
    public void schedule(Runnable runnable, String tag, long delay, TimeUnit unit) {
        if (threadPool instanceof ThreadPoolExecutor) {
            Future<?> request = ((ScheduledExecutorService) threadPool)
                    .schedule(runnable, delay, unit);
            addTask(request, tag);
        }
    }

    /**
     * 延时多久开始周期执行任务
     *
     * @param runnable
     * @param tag
     * @param delay
     * @param period
     * @param unit
     */
    public void scheduleAtFixedRate(Runnable runnable, String tag,
                                    long delay, long period, TimeUnit unit) {
        if (threadPool instanceof ThreadPoolExecutor) {
            Future<?> request = ((ScheduledExecutorService) threadPool).scheduleAtFixedRate(
                    runnable, delay, period, unit);
            addTask(request, tag);
        }
    }

    /**
     * 添加任务
     *
     * @param request
     * @param tag
     */
    private void addTask(Future<?> request, String tag) {
        synchronized (ThreadPoolManager.class) {
            if (tag != null) {
                List<WeakReference<Future<?>>> requestList = taskMap.get(tag);
                if (requestList == null) {
                    requestList = new LinkedList<>();
                    taskMap.put(tag, requestList);
                }
                requestList.add(new WeakReference<Future<?>>(request));
            }
        }
    }

    /**
     * 取消任务
     *
     * @param tag
     * @param mayInterruptIfRunning
     */
    public void cancelTask(String tag, boolean mayInterruptIfRunning) {
        synchronized (ThreadPoolManager.class) {
            List<WeakReference<Future<?>>> requestList = taskMap.get(tag);
            if (requestList != null) {
                for (WeakReference<Future<?>> requestRef : requestList) {
                    Future<?> request = requestRef.get();
                    if (request != null) {
                        request.cancel(mayInterruptIfRunning);
                    }
                }
            }
            taskMap.remove(tag);
        }
    }

    /**
     * 取消所有任务
     */
    public void cancelAllTasks() {
        for (String cls : taskMap.keySet()) {
            List<WeakReference<Future<?>>> requestList = taskMap.get(cls);
            if (requestList != null) {
                for (WeakReference<Future<?>> requestRef : requestList) {
                    Future<?> request = requestRef.get();
                    if (request != null) {
                        request.cancel(true);
                    }
                }
            }
        }
        taskMap.clear();
    }

    /**
     * 取消所有任务，通过迭代的方式
     */
    private void cancelAllTasksByIterator() {
        for (String clsName : taskMap.keySet()) {
            List<WeakReference<Future<?>>> requestList = taskMap.get(clsName);
            if (requestList != null) {
                Iterator<WeakReference<Future<?>>> iterator = requestList.iterator();
                while (iterator.hasNext()) {
                    Future<?> request = iterator.next().get();
                    if (request != null) {
                        request.cancel(true);
                    }
                }
            }
        }
        taskMap.clear();
    }
}
