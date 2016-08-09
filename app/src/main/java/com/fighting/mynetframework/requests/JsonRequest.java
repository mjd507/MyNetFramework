package com.fighting.mynetframework.requests;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public class JsonRequest extends Request<JSONObject>{

    public JsonRequest(HttpMethod method, String url, RequestListener<JSONObject> listener) {
        super(method, url, listener);
    }

    @Override
    public JSONObject parseResponse(Response response) {
        String jsonStr = new String(response.getRawData());
        try {
            return new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
