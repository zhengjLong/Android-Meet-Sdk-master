package com.library.base.view;


import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 自定义倒计时类
 */
public class MyCountDownTimer extends CountDownTimer {

    /**
     * @Fields count : 倒计时
     */
    private static int count = 60 * 1000;
    private static int countDownInterval = 1000;

    private Button mGetCodeBtn;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.mGetCodeBtn = button;
    }

    public MyCountDownTimer(Button button) {
        super(count, countDownInterval);
        this.mGetCodeBtn = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCodeBtn.setEnabled(false);
        mGetCodeBtn.setText(String.valueOf(millisUntilFinished / 1000) + "s后重新发送");
    }

    @Override
    public void onFinish() {
        mGetCodeBtn.setEnabled(true);
        mGetCodeBtn.setText("获取验证码");

    }

}
