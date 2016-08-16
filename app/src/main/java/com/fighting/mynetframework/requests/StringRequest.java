package com.fighting.mynetframework.requests;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public class StringRequest extends Request<String> {

    public StringRequest(HttpMethod method, String url, RequestListener<String> listener) {
        super(method, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
        return new String(response.getRawData());
    }
}
