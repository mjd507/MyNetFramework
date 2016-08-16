package com.fighting.mynetframework.core;

import com.fighting.mynetframework.httpstacks.HttpStack;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public final class SimpleNet {
    /**
     * 创建一个请求队列,NetworkExecutor数量为默认的数量
     */
    public static RequestQueue newRequestQueue() {
        return newRequestQueue(RequestQueue.DEFAULT_CORE_NUMS);
    }
    /**
     * 创建一个请求队列,NetworkExecutor数量为coreNums
     */
    public static RequestQueue newRequestQueue(int coreNums) {
        return newRequestQueue(coreNums, null);
    }

    /**
     * 创建一个请求队列,NetworkExecutor数量为coreNums
     */
    public static RequestQueue newRequestQueue(int coreNums, HttpStack httpStack) {
        RequestQueue queue = new RequestQueue(Math.max(0, coreNums), httpStack);
        queue.start();
        return queue;
    }

}
