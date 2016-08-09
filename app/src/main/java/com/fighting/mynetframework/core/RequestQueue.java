package com.fighting.mynetframework.core;

import android.util.Log;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.httpstacks.HttpStack;
import com.fighting.mynetframework.httpstacks.HttpStackFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public final class RequestQueue {

    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<Request<?>>();
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
    private int mDispatcherNums = DEFAULT_CORE_NUMS;
    private NetworkExecutor[] mDispatchers = null;
    /**Http请求的真正执行者*/
    private HttpStack mHttpStack;

    protected RequestQueue(int coreNums, HttpStack httpStack) {
        mDispatcherNums = coreNums;
        mHttpStack = httpStack != null ? httpStack : HttpStackFactory.createHttpStack();
    }

    /**
     * 启动NetworkExecutor
     */
    private final void startNetworkExecutors() {
        mDispatchers = new NetworkExecutor[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            mDispatchers[i] = new NetworkExecutor(mRequestQueue, mHttpStack);
            mDispatchers[i].start();
        }
    }

    public void start() {
        stop();
        startNetworkExecutors();
    }

    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].quit();
            }
        }
    }

    public void addRequest(Request<?> request) {
        if (!mRequestQueue.contains(request)) {
            request.setSerialNumber(this.generateSerialNumber());
            mRequestQueue.add(request);
        } else {
            Log.d("", "### 请求队列中已经含有");
        }
    }

    public void clear() {
        mRequestQueue.clear();
    }

    public BlockingQueue<Request<?>> getAllRequests() {
        return mRequestQueue;
    }

    /**
     * 为每个请求生成一个系列号
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }




}
