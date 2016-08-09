package com.fighting.mynetframework.httpstacks;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public interface HttpStack {

    Response performRequest(Request<?> request);
}
