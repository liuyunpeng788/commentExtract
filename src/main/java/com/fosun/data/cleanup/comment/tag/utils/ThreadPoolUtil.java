package com.fosun.data.cleanup.comment.tag.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 * @author: liumch
 * @create: 2019/7/3 17:03
 **/

@Component
public class ThreadPoolUtil {
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final int SIZE = 100;

    @Bean
    public ThreadPoolExecutor getPoolService(){
        ThreadPoolExecutor poolService = new ThreadPoolExecutor(CORES, CORES * 2, 10, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(SIZE), new ThreadFactory() {
            final static String prefix = "Serial-Time-Task-ThreadPool-";
            AtomicInteger threadNum = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,prefix + threadNum.getAndIncrement());
                if(thread.isDaemon()){
                    thread.setDaemon(false);
                }
                if(thread.getPriority() != Thread.NORM_PRIORITY){
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        },new ThreadPoolExecutor.CallerRunsPolicy());
        return poolService;
    }


}
