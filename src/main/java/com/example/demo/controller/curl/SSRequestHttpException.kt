package com.example.demo.controller.curl

class SSRequestHttpException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, error: Exception): super(message, error)

}