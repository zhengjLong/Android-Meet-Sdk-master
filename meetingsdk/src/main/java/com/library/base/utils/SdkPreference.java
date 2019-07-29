package com.library.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.library.base.bean.UserInfo;


/**
 * 轻量级数据存储
 * @author  jerome
 */
public class SdkPreference {

    private static final String  KEY_REFRESH_TOKEN = "refresh_token";
    private static final String  KEY_LOGIN_IP = "login_ip";
    private static final String  KEY_USER_NAME = "user_name";
    private static final String  KEY_LOGIN_PWD = "login_pwd";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_CAMERA = "camera_set";
    private static final String KEY_MICROPHONE = "microphone_set";
    private static final String KEY_MEETING_REMIND = "meeting_remind_set";
    private static final String KEY_MEETING_TITLE = "meeting_title_set";
    public static final String REMOTEID = "remoteId";
    private static final String KEY_USER_INFO = "userInfo";
    private static SdkPreference instance;
    private SharedPreferences sp;


    /**
     * application 初始化
     * @param context
     * @return
     */
    public static SdkPreference instance(Context context) {
        if (instance == null) {
            instance = new SdkPreference(context);
        }
        return instance;
    }


    public static SdkPreference getInstance() {
        return instance;
    }

    public SdkPreference(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * 保存用户token
     * @param token
     */
    public void saveToken(String token) {
        putString(KEY_TOKEN, token);
    }

    /**
     * 获得用户token
     * @return
     */
    public String getToken() {
        return getString(KEY_TOKEN, "");
    }


    /**
     * 保存设备唯一标识
     */
    public void saveUUID(String uuid) {
        putString(KEY_UUID, uuid);
    }

    /**
     * 获得设置唯一标识
     */
    public String getUUID() {
        return getString(KEY_UUID, "");
    }

    /**
     * 保存摄像头配置
     * @param camera
     */
    public void saveCameraSet(Boolean camera) {
        putBoolean(KEY_CAMERA, camera);
    }

    /**
     * 获得摄像头配置
     * @return
     */
    public Boolean getCameraSet() {
        return getBoolean(KEY_CAMERA, false);
    }

    /**
     * 保存麦克风配置
     * @param microphone
     */
    public void saveMicrophoneSet(Boolean microphone) {
        putBoolean(KEY_MICROPHONE, microphone);
    }

    /**
     * 获得麦克风配置
     * @return
     */
    public Boolean getMicrophoneSet() {
        return getBoolean(KEY_MICROPHONE, false);
    }

    /**
     * 保存会议提醒配置
     * @param remind
     */
    public void saveMeetingRemind(Boolean remind) {
        putBoolean(KEY_MEETING_REMIND, remind);
    }

    /**
     * 获得会议提醒配置
     * @return
     */
    public Boolean getMeetingRemind() {
        return getBoolean(KEY_MEETING_REMIND, false);
    }

    /**
     * 保存会议昵称配置
     * @param title
     */
    public void saveMeetingTitle(Boolean title) {
        putBoolean(KEY_MEETING_TITLE, title);
    }


    /**
     * 获得会议昵称配置
     * @return
     */
    public Boolean getMeetingTitle() {
        return getBoolean(KEY_MEETING_TITLE, false);
    }

    public void saveRefreshToken(String token) {
        putString(KEY_REFRESH_TOKEN, token);
    }

    public String getRefreshToken() {
        return getString(KEY_REFRESH_TOKEN, null);
    }

    /**
     * 保存用户数据
     */
    public void saveUserInfo(UserInfo info){
        if (!CheckUtil.isNull(info))
        putString(KEY_USER_INFO, new Gson().toJson(info));
    }

    /**
     * 获得用户数据
     */
    public UserInfo getUserInfo(){
        if (!CheckUtil.isNull(getString(KEY_USER_INFO,"")))
        return new Gson().fromJson(getString(KEY_USER_INFO,""),UserInfo.class);
        else
            return null;
    }
    /**
     * 登录ip
     * @param ip
     */
    public void saveLoginIP(String ip){
        putString(KEY_LOGIN_IP,ip);
    }

    /**
     * 获得登录ip
     * @return
     */
    public String getLoginIP(){
        return getString(KEY_LOGIN_IP,"");
    }
    /**
     * 默认用户名
     */
    public void saveUserName(String name){
        putString(KEY_USER_NAME,name);
    }

    /**
     * 获得默认用户名
     * @return
     */
    public String getUserName(){
        return getString(KEY_USER_NAME,"");
    }
    /**
     * 登录密码
     * @param pwd
     */
    public void saveLoginPwd(String pwd){
        putString(KEY_LOGIN_PWD,pwd);
    }

    /**
     * 获得登录密码
     * @return
     */
    public String getLoginPwd(){
        return getString(KEY_LOGIN_PWD,"");
    }




    /**
     * 清除用户信息 - 退出登录
     */
    public void clearUserInfo() {
        SharedPreferences.Editor e = sp.edit();
        e.remove(KEY_USER_INFO);
        e.remove(KEY_TOKEN);
        e.remove(KEY_REFRESH_TOKEN);
        e.apply();
    }



    public void clearValue(String key) {
        sp.edit().remove(key).apply();
    }

    public void putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defa) {
        return sp.getBoolean(key, defa);
    }

    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public String getString(String key, String defau) {
        return sp.getString(key, defau);
    }

    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    public int getInt(String key, int defa) {
        return sp.getInt(key, defa);
    }

    public long getLong(String key, long defa) {
        return sp.getLong(key, defa);
    }

    public void clearData() {
        sp.edit().clear().apply();
    }

    public void remove(String key) {
        sp.edit().remove(key).apply();
    }

}
