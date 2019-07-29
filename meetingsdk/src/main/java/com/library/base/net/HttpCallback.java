package com.library.base.net;


/**
 * HTTP响应
 *
 * @author  jerome
 */
public interface HttpCallback<T> {

    void onSuccess(T data);

    void onFailure(int errorCode, String message);
}
