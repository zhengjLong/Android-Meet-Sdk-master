package com.library.base;

/**
* zoom错误代码
*
* @author  jerome
*/
public enum MeetingErrorCode {


    //============================初始化失败统一返回自定义错误码===================================
    MEETING_ERROR_INIT(-1000008, "授权错误，请检查网络连接或联系管理员"),

    //============================zoom 错误码===================================
    MEETING_ERROR_SUCCESS(0, "开会成功"),
    MEETING_ERROR_INCORRECT_MEETING_NUMBER(1, "错误的会议号"),
    MEETING_ERROR_TIMEOUT(2, "开启会议超时"),
    MEETING_ERROR_NETWORK_UNAVAILABLE(3, "网络错误"),
    MEETING_ERROR_CLIENT_INCOMPATIBLE(4, "当前应用版本过低，请更新app"),
    MEETING_ERROR_NETWORK_ERROR(5, "网络错误"),
    MEETING_ERROR_MMR_ERROR(6, "MMR服务错误"),
    MEETING_ERROR_SESSION_ERROR(7, "Session错误"),
    MEETING_ERROR_MEETING_OVER(8, "会议已结束"),
    MEETING_ERROR_MEETING_NOT_EXIST(9, "会议不存在"),
    MEETING_ERROR_USER_FULL(10, "参会者人数超过上限"),
    MEETING_ERROR_NO_MMR(11, "当前会议没有可用的MMR服务器"),
    MEETING_ERROR_LOCKED(12, "会议已锁定"),
    MEETING_ERROR_RESTRICTED(13, "会议受到限制"),
    MEETING_ERROR_RESTRICTED_JBH(14, "不允许在主持人之前加入会议"),
    MEETING_ERROR_WEB_SERVICE_FAILED(15, "请求Web服务失败"),
    MEETING_ERROR_REGISTER_WEBINAR_FULL(16, "注册数超过了网络会议的上限"),
    MEETING_ERROR_DISALLOW_HOST_RESGISTER_WEBINAR(17, "不允许在主持人的电子邮件中注册网络会议"),
    MEETING_ERROR_DISALLOW_PANELIST_REGISTER_WEBINAR(18, "不允许在参会者的电子邮件中注册网络会议。"),
    MEETING_ERROR_HOST_DENY_EMAIL_REGISTER_WEBINAR(19, "网络研讨会的注册被主持人拒绝。"),
    MEETING_ERROR_WEBINAR_ENFORCE_LOGIN(20, "如果用户想参加网络会议，用户需要登录。"),
    MEETING_ERROR_EXIT_WHEN_WAITING_HOST_START(21, "已离开会议"),
    MEETING_ERROR_INVALID_ARGUMENTS(99, "参数无效"),
    MEETING_ERROR_UNKNOWN(100, "未知错误"),
    MEETING_ERROR_INVALID_STATUS(101, "操作失败。请再试一次");

   private int errorCode;
   private String message;

   MeetingErrorCode(int code, String msg) {
       this.errorCode = code;
       this.message = msg;
   }

   public static MeetingErrorCode getValue(int errorCode) {
       MeetingErrorCode[] values = MeetingErrorCode.values();
       for (MeetingErrorCode val : values) {
           if (val.errorCode == errorCode) {
               return val;
           }
       }
       return MEETING_ERROR_UNKNOWN;
   }

   public int getErrorCode() {
       return errorCode;
   }

   public String getMessage() {
       return message;
   }

}
