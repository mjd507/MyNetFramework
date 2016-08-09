package com.fighting.mynetframework.cache;

import com.fighting.mynetframework.base.Response;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public class LruMemCache implements Cache<String, Response> {

    @Override
    public Response get(String key) {
        return null;
    }

    @Override
    public void put(String key, Response value) {

    }

    @Override
    public void remove(String key) {

    }
}
