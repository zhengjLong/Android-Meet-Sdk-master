package com.library.base.bean

/**
 *
 * 用户登录成功返回数据
 * @author jerome
 * @version 2018/11/20
 */
class LoginInfo {

    var data: DataBean? = null
    var message: String? = null
    var status: String? = null
    var statusCode: Int = 0
    var timestamp: String? = null

    class DataBean {

        var token: String? = null
        var menuCodeList: List<String>? = null
    }
}
