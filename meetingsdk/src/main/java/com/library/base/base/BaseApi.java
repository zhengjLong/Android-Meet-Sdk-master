package com.library.base.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.library.base.BuildConfig;
import com.library.base.bean.LoginInfo;
import com.library.base.bean.UserInfo;
import com.library.base.net.ApiResponseModel;
import com.library.base.net.HttpCallback;
import com.library.base.net.HttpEngine;
import com.library.base.utils.CheckUtil;
import com.library.base.utils.Logcat;
import com.library.base.utils.SdkPreference;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求Api类
 * @author jerome
 */
public class BaseApi {


//    url短路径
    private static String baseUrl;
//    上传错误日志
    private static String uploadErrorData;
//    获得刷新token
    private static String getRefreshToken;
//    登录
    private static String login;
//    获得用户数据
    private static String getUserInfo;



    private HttpEngine httpEngine;
    private static WeakReference<BaseApi> instance;
    private final Context mContext;

    public BaseApi(Context context, Handler handler) {
        mContext = context.getApplicationContext();
        httpEngine = HttpEngine.getInstance(context);
        httpEngine.setHandler(handler);
        Logcat.INSTANCE.e("SdkPreference.getInstance().getLoginIP()== "+SdkPreference.getInstance().getLoginIP());
        initApi(CheckUtil.isNull(SdkPreference.getInstance().getLoginIP()) ? BuildConfig.APP_BASE_RUL:SdkPreference.getInstance().getLoginIP());
        Logcat.INSTANCE.e("new BaseApi initApi  "+baseUrl);
    }

    public void initApi(String url) {
        Logcat.INSTANCE.e("initApi "+url);

        baseUrl = url;
        uploadErrorData = baseUrl + "url";
        getRefreshToken = baseUrl + "url";
        login = baseUrl + "login";
        getUserInfo = baseUrl + "user/getSessionUser";
    }

    public synchronized static BaseApi getInstance(Context context, Handler handler) {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new BaseApi(context, handler));
        }
        return instance.get();
    }

    /**
     * get default main thread looper server api
     *
     * @param context
     * @return
     */
    public synchronized static BaseApi getInstance(Context context) {
        return getInstance(context, new Handler(Looper.getMainLooper()));
    }



    /**
     * 上传错误日志
     *
     * @param callback
     */
    public void uploadErrorData(String content, HttpCallback<String> callback) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("content", content);
        httpEngine.post(uploadErrorData, paramMap, String.class, callback);
    }
    /**
     * 获得刷新token
     */
    public ApiResponseModel getRefreshToken(String refreshToken) {
        Map<String, String> params = new HashMap<>();
        params.put("refreshToken", refreshToken);
        return  httpEngine.syncPost(getRefreshToken, params, null);
    }
    /**
     * 登录
     */
    public void login(String userName,String pwd,HttpCallback<LoginInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("tenantUri", userName);
        params.put("password", pwd);
         httpEngine.post(login, params,LoginInfo.class, callback);
    }

    /**
     * 获得用户数据
     */
    public void getUserInfo(HttpCallback<UserInfo> callback) {
        httpEngine.get(getUserInfo,null,UserInfo.class,callback);
    }

}
