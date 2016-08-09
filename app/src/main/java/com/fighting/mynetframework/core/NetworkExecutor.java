package com.fighting.mynetframework.core;

import android.util.Log;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;
import com.fighting.mynetframework.cache.Cache;
import com.fighting.mynetframework.cache.LruMemCache;
import com.fighting.mynetframework.httpstacks.HttpStack;

import java.util.concurrent.BlockingQueue;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public class NetworkExecutor extends Thread {
    private BlockingQueue<Request<?>> mRequestQueue;
    private HttpStack mHttpStack;
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();
    private static Cache<String, Response> mReqCache = new LruMemCache();
    private boolean isStop = false;

    public NetworkExecutor(BlockingQueue<Request<?>> queue, HttpStack httpStack) {
        mRequestQueue = queue;
        mHttpStack = httpStack;
    }

    @Override
    public void run() {
        try {
            while (!isStop) {
                final Request<?> request = mRequestQueue.take();
                if (request.isCanceled()) {
                    Log.d("### ", "### 取消执行了");
                    continue;
                }
                Response response = null;
                if (isUseCache(request)) {
                    // 从缓存中取
                    response = mReqCache.get(request.getUrl());
                } else {
                    // 从网络上获取数据
                    response = mHttpStack.performRequest(request);
                    // 如果该请求需要缓存,那么请求成功则缓存到mResponseCache中
                    if (request.shouldCache() && isSuccess(response)) {
                        mReqCache.put(request.getUrl(), response);
                    }
                }

                // 分发请求结果
                mResponseDelivery.deliveryResponse(request, response);
            }
        } catch (InterruptedException e) {
            Log.i("", "### 请求分发器退出");
        }

    }
    private boolean isSuccess(Response response) {
        return response != null && response.getStatusCode() == 200;
    }

    private boolean isUseCache(Request<?> request) {
        return request.shouldCache() && mReqCache.get(request.getUrl()) != null;
    }

    public void quit() {
        isStop = true;
        interrupt();
    }

}
