package com.library.base.bean

/**
 * 调用分享sdk携带参数
 * @author jerome
 * @version 2018/11/28
 */
class ShareParams {

    var platformType :Int = 1// 1:微信聊天  2:微信朋友圈  3:微信收藏  27:钉钉
    var contentType :Int = 0 // 0:文本  1:图片  2:网页


    var text :String = ""//文本


    var imageUrlShare : String=""// 分享图片
    var imageUrlThumb : String=""// 缩略图

    var title : String=""// 标题
    var description : String=""// 描述
    var webPageUrl : String=""// 链接

}