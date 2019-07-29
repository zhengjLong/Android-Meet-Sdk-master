package com.library.base.net;

import android.os.Handler;

import com.google.gson.Gson;


/**
 * HTTP响应回调
 *
 * @author  jerome
 */
class HttpResponseCallBack<T> extends BaseHttpResponse<T> {

    private static final Gson GSON = new Gson();
    private final Class<T> mClass;

    public HttpResponseCallBack(Class<T> cls, HttpCallback<T> callback, Handler handler) {
        super(callback, handler);
        mClass = cls;
    }

    @Override
    protected void onParseDataJson(String msg, String dataJson) {

        if (Void.class == mClass) {
            notifySuccess(null);
            return;
        }

        if (String.class == mClass) {
            notifySuccess((T) dataJson);
            return;
        }

        T data = GSON.fromJson(dataJson, mClass);
        if (data == null) {
            notifyFailure(ApiErrorCode.ERROR_EMPTY_DATA, msg);
        } else {
            notifySuccess(data);
        }
    }

}
