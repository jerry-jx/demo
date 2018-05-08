package com.jxinternet.platform.dsf.external.ss.api.curl

class HanderSuccessfulCodeException: RuntimeException {

    constructor(message: String): super(message)

    constructor(message: String, error: Exception): super(message, error)

}