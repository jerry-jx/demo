package com.example.demo.controller.curl

import com.fasterxml.jackson.annotation.JsonProperty

abstract class SSReq {

    @get:JsonProperty("MerchantCode")
    abstract val merchantCode: String

}