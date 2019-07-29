package com.library.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.textservice.TextInfo
import android.widget.Toast
import com.library.base.dialog.LoadingDialog
import com.library.base.utils.CheckUtil
import com.library.base.utils.Logcat
import com.library.base.utils.SdkUtil
import com.zipow.videobox.sdk.ZoomStatus.*
import us.zoom.sdk.*
import us.zoom.sdk.MeetingError.MEETING_ERROR_INVALID_ARGUMENTS
import us.zoom.sdk.RoomSystemDevice.ROOMDEVICE_H323
import us.zoom.sdk.RoomSystemDevice.ROOMENCRYPT_AUTO


/**
 * 会议工具类
 * @author jerome
 * @version 2018/3/27
 */
class MeetingUtils : ZoomSDKInitializeListener, MeetingServiceListener, InviteRoomSystemListener {


    //    sdk统一回调
    private var callBack: MeetingCallBack? = null

    private var connectionCount = 5//硬件会议断后重连接

    private var resetCount = 5 //剩余连接数

    private var currentHardwareId = "" //硬件地址


    /**
     * 初始化sdk:只初始化一次
     */
    fun initSdk(context: Context, callBack: MeetingCallBack) {

        val sdk = ZoomSDK.getInstance()

        this.callBack = callBack

        if (!sdk.isInitialized) {
//            进行初始化
            sdk.initialize(context, BuildConfig.UMEETING_KEY,
                    BuildConfig.UMEETING_SECRET,
                    BuildConfig.UMEETING_DOMAIN, this)
        } else {
//            添加注册监听
            registerMeetingServiceListener()
        }

    }


    /**
     * 必须同步生命周期关闭
     */
    fun destroy() {
//        关闭会议监听
        val zoomSDK = ZoomSDK.getInstance()

        if (zoomSDK.isInitialized) {
            val meetingService = zoomSDK.meetingService
            meetingService.removeListener(this)
            meetingService.inviteRoomSystemHelper.removeEventListener(this)
        }
    }


    /**
     * 注册会议回调监听
     */
    private fun registerMeetingServiceListener() {
        val zoomSDK = ZoomSDK.getInstance()
        val meetingService = zoomSDK.meetingService
        meetingService?.addListener(this)
    }


    /**
     * sdk初始化回调
     */
    override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {

        Logcat.e("onZoomSDKInitializeResult errorCode is $errorCode,internalErrorCode is $internalErrorCode")
//        初始化失败
        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            callBack?.meetingErrorMessage(errorCode, "${MeetingErrorCode.MEETING_ERROR_INIT.message}，错误代码：$errorCode")
        } else {
            registerMeetingServiceListener()
        }
    }


    /**
     * 会议事件监听回调
     */
    override fun onMeetingStatusChanged(meetingStatus: MeetingStatus, errorCode: Int, internalErrorCode: Int) {

        Logcat.e("onMeetingStatusChanged MeetingStatus is ${meetingStatus.name} , errorCode is $errorCode,internalErrorCode is $internalErrorCode")


//        提示错误信息
        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
            callBack?.meetingErrorMessage(errorCode, MeetingErrorCode.getValue(errorCode).message)
        }

//       会议掉线
        if (meetingStatus == MeetingStatus.MEETING_STATUS_DISCONNECTING) {
            callBack?.meetingDisconnected()
        }

//        已连接会议
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            callBack?.meetingSuccessCallBack()
        }

    }


    /**
     * 加入会议
     */
    fun joinMeeting(context: Context, meetingNo: String, meetingPassword: String, displayName: String, inviteFunction: Boolean) {

        SdkUtil.getUserPermission(SdkUtil.PermissionCallBack {
            if (it) {
                if (TextUtils.isEmpty(meetingNo)) {
                    callBack?.meetingErrorMessage(MeetingErrorCode.MEETING_ERROR_INVALID_ARGUMENTS.errorCode, MeetingErrorCode.MEETING_ERROR_INVALID_ARGUMENTS.message)
                    return@PermissionCallBack
                }

                val zoomSDK = ZoomSDK.getInstance()
                val meetingService = zoomSDK.meetingService


                if (!zoomSDK.isInitialized) {
                    callBack?.meetingErrorMessage(MeetingErrorCode.MEETING_ERROR_INIT.errorCode, MeetingErrorCode.MEETING_ERROR_INIT.message)
                    return@PermissionCallBack
                }

                if (meetingService.meetingStatus != MeetingStatus.MEETING_STATUS_IDLE) {
                    //会议还在进行
                    meetingService.returnToMeeting(context)
                    return@PermissionCallBack
                }

                val meetingOption = JoinMeetingOptions()

//		          meetingOption.no_driving_mode = true
//                meetingOption.no_meeting_end_message = true
//                meetingOption.no_titlebar = true
//                meetingOption.no_bottom_toolbar = true
//                meetingOption.no_dial_in_via_phone = true
//                meetingOption.no_dial_out_to_phone = true
//                meetingOption.no_disconnect_audio = true//(true:不显示更多里面“ 断开音频 ”)
//                meetingOption.no_share = true
//                meetingOption.invite_options = InviteOptions.INVITE_VIA_EMAIL + InviteOptions.INVITE_VIA_SMS
//                meetingOption.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE
//                meetingOption.no_audio = true//断开音频，包括我的麦克风与对方的音频
//                meetingOption.no_video = false
//                meetingOption.no_meeting_error_message = true
//                meetingOption.participant_id = "participant id"


                if (inviteFunction) {
                    meetingOption.no_invite = false//显示邀请功能
                    meetingOption.invite_options = InviteOptions.INVITE_COPY_URL//邀请功能为：复制链接
                } else {
                    meetingOption.no_invite = true//不显示邀请功能
                }
                ZoomSDK.getInstance().meetingSettingsHelper.setMuteMyMicrophoneWhenJoinMeeting(true)//关闭我的麦克风

                val meetingParams = JoinMeetingParams()
                meetingParams.displayName = displayName
                meetingParams.meetingNo = meetingNo
                meetingParams.password = meetingPassword
                val ret = meetingService.joinMeetingWithParams(context, meetingParams, meetingOption)
            }


        }, context as Activity, Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    }


    /**
     * 级联会议
     */
    fun hardwareMeeting(hardwareId: String) {

        val zoomSDK = ZoomSDK.getInstance()

        if (!zoomSDK.isInitialized) {
            callBack?.meetingErrorMessage(MeetingErrorCode.MEETING_ERROR_INIT.errorCode, MeetingErrorCode.MEETING_ERROR_INIT.message)
            return
        }

        if (CheckUtil.isNull(hardwareId)) {
            callBack?.meetingErrorMessage(MeetingErrorCode.MEETING_ERROR_INVALID_ARGUMENTS.errorCode, MeetingErrorCode.MEETING_ERROR_INVALID_ARGUMENTS.message)
            return
        }

//        记录连接次数
        resetCount--
        currentHardwareId = hardwareId

        val meetingService = zoomSDK.meetingService

        meetingService.inviteRoomSystemHelper.addEventListener(this)
        var roomSystemDevice = RoomSystemDevice("", hardwareId, "", ROOMDEVICE_H323, ROOMENCRYPT_AUTO)
        val isSuccess = meetingService.inviteRoomSystemHelper.callOutRoomSystem(roomSystemDevice)
        Logcat.e("========callOutRoomSystem===============" + isSuccess)
    }


    /**
     * 调用sdk开启会议
     */

    fun startMeeting(context: Context, meetingNo: String, userId: String, token: String, displayName: String, inviteFunction: Boolean) {

        SdkUtil.getUserPermission(SdkUtil.PermissionCallBack {
            if (it) {
                val zoomSDK = ZoomSDK.getInstance()

                if (TextUtils.isEmpty(meetingNo) || TextUtils.isEmpty(userId) || TextUtils.isEmpty(token) ||
                        TextUtils.isEmpty(displayName)) {
                    callBack?.meetingErrorMessage(MeetingErrorCode.MEETING_ERROR_INVALID_ARGUMENTS.errorCode, MeetingErrorCode.MEETING_ERROR_INVALID_ARGUMENTS.message)
                    return@PermissionCallBack
                }
                if (!zoomSDK.isInitialized) {
                    callBack?.meetingErrorMessage(MeetingErrorCode.MEETING_ERROR_INIT.errorCode, MeetingErrorCode.MEETING_ERROR_INIT.message)
                    return@PermissionCallBack
                }


                val meetingService = zoomSDK.meetingService

                if (meetingService.meetingStatus != MeetingStatus.MEETING_STATUS_IDLE) {
                    //会议还在进行
                    meetingService.returnToMeeting(context)
                    return@PermissionCallBack
                }

                val opts = StartMeetingOptions()
//                opts.no_driving_mode = true
//                opts.no_meeting_end_message = true
//                opts.no_titlebar = true
//                opts.no_bottom_toolbar = true
//                opts.no_dial_in_via_phone = true
//                opts.no_dial_out_to_phone = true
//                opts.no_disconnect_audio = false
//                opts.no_share = true
//                opts.invite_options = InviteOptions.INVITE_ENABLE_ALL
//                opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE + MeetingViewsOptions.NO_BUTTON_VIDEO
//                opts.no_meeting_error_message = true
//                opts.no_audio = false//音频功能开关
//                opts.no_video = true
                val params  = StartMeetingParams4APIUser()
                params.userId = userId
                params.zoomToken = token
                params.meetingNo = meetingNo
                params.displayName = displayName

                if (inviteFunction) {
                    opts.no_invite = false//显示邀请功能
                    opts.invite_options = InviteOptions.INVITE_COPY_URL//邀请功能为：复制链接
                } else {
                    opts.no_invite = true//不显示邀请功能
                }
                ZoomSDK.getInstance().meetingSettingsHelper.setMuteMyMicrophoneWhenJoinMeeting(true)//关闭我的麦克风
                val ret = meetingService.startMeeting(context, userId, token, MeetingService.USER_TYPE_API_USER, meetingNo, displayName, opts)
//                val ret = meetingService.startMeetingWithParams(context, params, opts)
            }

        }, context as Activity, Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)

    }


    //    =================级联会议 start===================
    override fun onParingRoomSystemResult(p0: InviteRoomSystemHelper.PairingRoomSystemResult?, p1: Long) {
        Logcat.e("onParingRoomSystemResult=================$p0")
    }

    override fun onCallOutRoomSystemStatusChanged(p0: InviteRoomSystemHelper.CallOutRoomSystemStatus?) {
        Logcat.e("onCallOutRoomSystemStatusChanged=================$p0")
//        超时重连sdk调用无效
//        reConnect(p0)
        when (p0) {
            InviteRoomSystemHelper.CallOutRoomSystemStatus.CallOutRoomSystem_Timeout,
            InviteRoomSystemHelper.CallOutRoomSystemStatus.CallOutRoomSystem_Failed,
            InviteRoomSystemHelper.CallOutRoomSystemStatus.CallOutRoomSystem_Unknown
            -> {
//                失败
                callBack?.hardwareMeetingState(false)
            }
            InviteRoomSystemHelper.CallOutRoomSystemStatus.CallOutRoomSystem_Success -> {
//                  成功
                callBack?.hardwareMeetingState(true)
            }
        }

    }

    //    =================级联会议 end===================


    interface MeetingCallBack {

        /**
         *
         * 错误回调
         */
        fun meetingErrorMessage(errorCode: Int, message: String)

        /**
         * 会议断开
         */
        fun meetingDisconnected()

        /**
         * 成功回调
         */
        fun meetingSuccessCallBack()

        /**
         * 硬件级联状态
         */
        fun hardwareMeetingState(success: Boolean)

    }

}
