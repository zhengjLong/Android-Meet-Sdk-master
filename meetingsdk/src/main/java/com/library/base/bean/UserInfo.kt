package com.library.base.bean

/**
 * 登录返回信息
 * @author jerome
 * @version 2018/11/19
 */
class UserInfo {

    var statusCode: Int = 0
    var timestamp: String? = null
    var data: DataBean? = null
    var message: String? = null

    class DataBean {

        var id: String? = null
        var name: String? = null
        var nameFullPinYin: String? = null
        var nameSimplePinYin: String? = null
        var createBy: String? = null
        var createTime: String? = null
        var updateBy: String? = null
        var updateTime: String? = null
        var remark: Any? = null
        var tenantId: String? = null
        var sort: Any? = null
        var deptId: Any? = null
        var account: String? = null
        var password: String? = null
        var email: Any? = null
        var phoneNumber: Any? = null
        var sex: Any? = null
        var avatar: Any? = null
        var status: Any? = null
        var roleIds: String? = null
        var uri: String? = null
        var deptIds: Any? = null
        var activeDirectoryFlag: Any? = null
        var postId: Any? = null
        var assignRoleIds: String? = null
        var maxOfSquare: Any? = null
        var cardNumber: Any? = null
    }
}
