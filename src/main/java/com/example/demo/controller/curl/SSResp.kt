package com.example.demo.controller.curl

import com.fasterxml.jackson.annotation.JsonProperty

abstract class SSResp : SSGlobalRespCode {

    /**
     * 响应代码
     */
    @get:JsonProperty("Code")
    abstract val code: Int

    /**
     * 返回信息
     */
    @get:JsonProperty("Message")
    abstract val message: String

    open fun check(): Boolean {
        return check(code, { false })
    }

    fun check(body: () -> Boolean): Boolean {
        return super.check(code, body)
    }


}



