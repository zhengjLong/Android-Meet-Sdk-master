package com.library.base.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import java.lang.ref.WeakReference
import java.util.*

/**
 * 文本内容状态检查
 *
 * @author jerome
 */
class TextCheckStatusUtil(vararg textViews: TextView) {

    private val mTextViews = ArrayList<WeakReference<TextView>>()

    private var onCompleteListener: OnCompleteListener? = null

    /**
     * 是否全部有内容
     *
     * @return
     */
    private val isAllHasContent: Boolean
        get() {
            return mTextViews.none { textView -> TextUtils.isEmpty(textView.get()?.text.toString().trim { it <= ' ' }) }
        }


    init {
        textViews.mapTo(mTextViews) { WeakReference(it) }
        init()
    }


    /**
     * 将传入的EditText与TextView设置文本变化监听
     */
    private fun init() {
        for (textView in mTextViews) {
            textView.get()?.addTextChangedListener(MyTextWatcher())
        }
    }


    /**
     * 清除引用
     */
    fun clear() {
        for (textView in mTextViews) {
            textView.clear()
        }
        mTextViews.clear()
    }

    /**
     * 监听文本变化
     */
    private inner class MyTextWatcher : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (null != onCompleteListener) {
                if (isAllHasContent) {
                    onCompleteListener!!.isInputComplete(true)
                } else {
                    onCompleteListener!!.isInputComplete(false)
                }
            } else {
                throw IllegalStateException("未初始化OnCompleteListener监听！！")
            }

        }
    }


    /**
     * 状态监听
     *
     * @param listener
     */
    fun setOnCompleteListener(listener: OnCompleteListener) {
        onCompleteListener = listener
    }

    /**
     * 监听是否全部填写完成接口
     */
    interface OnCompleteListener {

        /**
         * 是否完成
         *
         * @param isComplete
         */
        fun isInputComplete(isComplete: Boolean)
    }

}
