package com.fighting.mynetframework.httpstacks;

import android.net.http.AndroidHttpClient;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;
import com.fighting.mynetframework.config.HttpClientConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.Map;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public class HttpClientStack implements HttpStack {
    HttpClientConfig mConfig = HttpClientConfig.getConfig();
    HttpClient mHttpClient = AndroidHttpClient.newInstance(mConfig.userAgent);

    @Override
    public Response performRequest(Request<?> request) {
        try {
            HttpUriRequest httpRequest = createHttpRequest(request);
            // 添加连接参数
            setConnectionParams(httpRequest);
            // 添加header
            addHeaders(httpRequest, request.getHeaders());
            // https配置
            configHttps(request);
            // 执行请求
            HttpResponse response = mHttpClient.execute(httpRequest);
            // 构建Response
            Response rawResponse = new Response(response.getStatusLine());
            // 设置Entity
            rawResponse.setEntity(response.getEntity());
            return rawResponse;
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 如果是https请求,则使用用户配置的SSLSocketFactory进行配置.
     */
    private void configHttps(Request<?> request) {
        SSLSocketFactory sslSocketFactory = mConfig.getSocketFactory();
        if (request.isHttps() && sslSocketFactory != null) {
            Scheme sch = new Scheme("https", sslSocketFactory, 443);
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
        }
    }

    /**
     * 设置连接参数,这里比较简单啊.一些优化设置就没有写了.
     */
    private void setConnectionParams(HttpUriRequest httpUriRequest) {
        HttpParams httpParams = httpUriRequest.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, mConfig.connTimeOut);
        HttpConnectionParams.setSoTimeout(httpParams, mConfig.soTimeOut);
    }

    /**
     * 根据请求类型创建不同的Http请求
     */
    static HttpUriRequest createHttpRequest(Request<?> request) {
        HttpUriRequest httpUriRequest = null;
        switch (request.getHttpMethod()) {
            case GET:
                httpUriRequest = new HttpGet(request.getUrl());
                break;
            case DELETE:
                httpUriRequest = new HttpDelete(request.getUrl());
                break;
            case POST: {
                httpUriRequest = new HttpPost(request.getUrl());
                httpUriRequest.addHeader(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfNonEmptyBody((HttpPost) httpUriRequest, request);
            }
            break;
            case PUT: {
                httpUriRequest = new HttpPut(request.getUrl());
                httpUriRequest.addHeader(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfNonEmptyBody((HttpPut) httpUriRequest, request);
            }
            break;
            default:
                throw new IllegalStateException("Unknown request method.");
        }

        return httpUriRequest;
    }

    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    /**
     * 将请求参数设置到HttpEntity中
     */
    private static void setEntityIfNonEmptyBody(HttpEntityEnclosingRequestBase httpRequest,
                                                Request<?> request) {
        byte[] body = request.getBody();
        if (body != null) {
            HttpEntity entity = new ByteArrayEntity(body);
            httpRequest.setEntity(entity);
        }
    }
}
