package com.fighting.mynetframework.core;

import android.os.Handler;
import android.os.Looper;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;

import java.util.concurrent.Executor;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */
class ResponseDelivery implements Executor {

    /**
     * 主线程的handler
     */
    Handler mResponseHandler = new Handler(Looper.getMainLooper());

    /**
     * 处理请求结果,将其执行在UI线程
     */
    public void deliveryResponse(final Request<?> request, final Response response) {
        Runnable respRunnable = new Runnable() {

            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };

        execute(respRunnable);
    }

    @Override
    public void execute(Runnable command) {
        mResponseHandler.post(command);
    }

}
