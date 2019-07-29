package com.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.library.base.utils.SdkPreference;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * 程序跳转路由
 */
public class AppRoute {


    //region Activity堆栈管理
    private static  Stack<WeakReference<Activity>> sActivityStack = new Stack<>();

    /**
     * 压栈
     */
    public static void pushActivity(Activity activity) {
        sActivityStack.push(new WeakReference<>(activity));
    }

    /**
     * 移除
     */
    public static boolean removeActivity(Activity activity) {
        for (WeakReference<Activity> weakReference : sActivityStack) {
            if (weakReference.get() == activity) {
                sActivityStack.remove(weakReference);
                return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public static void exitApplication() {
        emptyActivity();
        SdkPreference.getInstance().clearUserInfo();
//        System.exit(0);
    }

    /**
     * 清空activity
     */
    public static void emptyActivity() {
        while (!sActivityStack.empty()) {
            WeakReference<Activity> reference = sActivityStack.pop();
            if (reference != null) {
                reference.get().finish();
            }
        }
    }

    /**
     * region 统一管理跳转
     * @param context
     * @param cls
     */
    private static void jump2Activity(Context context, Class<?> cls) {
        startActivity(context, new Intent(context, cls));
    }

    private static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private static void startActivityForResult(Activity context, Intent intent, int requestCode) {
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 登出
     *
     * @param context
     */
    public static void logout(Context context) {
        // 清除用户数据
        exitApplication();
    }

}
