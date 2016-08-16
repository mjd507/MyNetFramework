package com.fighting.mynetframework.requests;

import android.util.Log;

import com.fighting.mynetframework.base.Request;
import com.fighting.mynetframework.base.Response;
import com.fighting.mynetframework.entity.MultipartEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 描述： Multipart请求 ( 只能为POST请求 ),该请求可以搭载多种类型参数,比如文本、文件等,但是文件仅限于小文件,否则会出现OOM异常.
 * Created by MaJD on 2016/8/8.
 */
public class MultipartRequest extends Request<String> {
    MultipartEntity mMultiPartEntity = new MultipartEntity();

    public MultipartRequest(String url, RequestListener<String> listener) {
        super(HttpMethod.POST, url, listener);
    }

    public MultipartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 将MultipartEntity中的参数写入到bos中
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    public String parseResponse(Response response) {
        if (response != null && response.getRawData() != null) {
            return new String(response.getRawData());
        }

        return "";
    }
}
