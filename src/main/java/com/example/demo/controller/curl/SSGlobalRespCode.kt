package com.example.demo.controller.curl

/**
 * IMOne请求返回的code码
 */
interface SSGlobalRespCode {

    companion object {

        // 成功
        val SUCCESSFUL: Int = 0

        // 营运商代码无效
        private val INVALID_MERCHANT_CODE: Int = 500

        // 非法访问
        private val UNAUTHORIZED_ACCESS: Int = 501

        // 必须的参数不能为空
        private val REEQUIRED_FIELD_CANNOT_BE_EMPTY_OR_NULL: Int = 505

        // 设置正在进行中，请联络技术支援
        private val SETUP_IN_PROGRESS: Int = 538

        // 产品供应商内部错误
        private val PROVIDER_INTERNAL_ERROR: Int = 600

        // 非法产品访问
        private val UNAUTHORIZED_PRODUCT_ACCESS: Int = 601

        // 无效的参数值
        private val INVALID_ARGUMENT: Int = 612

        // 系统目前无法处理您的请求。请重试
        private val PLEASE_TRY_AGAIN: Int = 998

        // 系统处理您的请求失败
        private val SYSTEM_HAS_FAILED_TO_PROCESS_YOUR_REQUEST: Int = 999

    }

    fun check(code: Int, body: () -> Boolean): Boolean {

        return when (code) {
            SUCCESSFUL -> true

            INVALID_MERCHANT_CODE,
            UNAUTHORIZED_ACCESS,
            REEQUIRED_FIELD_CANNOT_BE_EMPTY_OR_NULL,
            SETUP_IN_PROGRESS,
            PROVIDER_INTERNAL_ERROR,
            UNAUTHORIZED_PRODUCT_ACCESS,
            INVALID_ARGUMENT,
            PLEASE_TRY_AGAIN,
            SYSTEM_HAS_FAILED_TO_PROCESS_YOUR_REQUEST -> false

            else -> body()
        }
    }

}