package com.fighting.mynetframework.core;

import com.fighting.mynetframework.base.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public final class RequestQueue {
    private BlockingQueue<Request<?>> mBlockingQueue = new PriorityBlockingQueue<Request<?>>();
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;

}
