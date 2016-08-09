package com.fighting.mynetframework.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public class HttpUrlConnConfig extends HttpConfig{

    private static HttpUrlConnConfig config = new HttpUrlConnConfig();
    private SSLSocketFactory sslSocketFactory = null;
    private HostnameVerifier hostnameVerifier = null;

    private HttpUrlConnConfig() {
    }

    public static HttpUrlConnConfig getConfig() {
        return config;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHttpsConfig(SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier) {
        sslSocketFactory = sslSocketFactory;
        hostnameVerifier = hostnameVerifier;
    }
}
