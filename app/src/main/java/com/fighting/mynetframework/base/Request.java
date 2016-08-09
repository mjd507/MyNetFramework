package com.fighting.mynetframework.base;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */
public abstract class Request<T> implements Comparable<Request<T>> {


    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        /** http request type */
        private String mHttpMethod = "";

        private HttpMethod(String method) {
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    public static enum Priority {
        LOW,
        NORMAL,
        HIGN,
        IMMEDIATE
    }


    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    public final static String HEADER_CONTENT_TYPE = "Content-Type";

    protected int mSerialNum = 0;

    protected Priority mPriority = Priority.NORMAL;

    protected boolean isCancel = false;

    /** 该请求是否应该缓存 */
    private boolean mShouldCache = true;

    protected RequestListener<T> mRequestListener;

    private String mUrl = "";

    HttpMethod mHttpMethod = HttpMethod.GET;

    private Map<String, String> mHeaders = new HashMap<String, String>();

    private Map<String, String> mBodyParams = new HashMap<String, String>();

    public Request(HttpMethod method, String url, RequestListener<T> listener) {
        mHttpMethod = method;
        mUrl = url;
        mRequestListener = listener;
    }

    public void addHeader(String name, String value) {
        mHeaders.put(name, value);
    }

    /**
     * 从原生的网络请求中解析结果
     */
    public abstract T parseResponse(Response response);

    /**
     * 处理Response,该方法运行在UI线程.
     */
    public final void deliveryResponse(Response response) {
        T result = parseResponse(response);
        if (mRequestListener != null) {
            int stCode = response != null ? response.getStatusCode() : -1;
            String msg = response != null ? response.getMessage() : "unkown error";
            Log.e("", "### 执行回调 : stCode = " + stCode + ", result : " + result + ", err : " + msg);
            mRequestListener.onComplete(stCode, result, msg);
        }
    }

    public String getUrl() {
        return mUrl;
    }

    public RequestListener<T> getRequestListener() {
        return mRequestListener;
    }

    public int getSerialNumber() {
        return mSerialNum;
    }

    public void setSerialNumber(int mSerialNum) {
        this.mSerialNum = mSerialNum;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Map<String, String> getParams() {
        return mBodyParams;
    }

    public boolean isHttps() {
        return mUrl.startsWith("https");
    }

    /**
     * 该请求是否应该缓存
     */
    public void setShouldCache(boolean shouldCache) {
        this.mShouldCache = shouldCache;
    }

    public boolean shouldCache() {
        return mShouldCache;
    }

    public void cancel() {
        isCancel = true;
    }

    public boolean isCanceled() {
        return isCancel;
    }

    /**
     * Returns the raw POST or PUT body to be sent.
     */
    public byte[] getBody() {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    @Override
    public int compareTo(Request<T> another) {
        Priority myPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return myPriority.equals(anotherPriority) ? this.getSerialNumber()
                - another.getSerialNumber()
                : myPriority.ordinal() - anotherPriority.ordinal();
    }

    /**
     * 网络请求Listener
     */
    public static interface RequestListener<T> {

        void onComplete(int stCode, T response, String errMsg);
    }
}