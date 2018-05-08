package com.jxinternet.platform.dsf.external.ss.api.curl

import com.example.demo.controller.curl.SSReq
import com.example.demo.controller.curl.SSRequestHttpException
import com.example.demo.controller.curl.SSResp
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Callable
import java.util.concurrent.Executors


@Component
class SSCurlNetWork(
        @Qualifier("ssRestTemplate")
        private val restTemplate: RestTemplate,

        private val objectMapper: ObjectMapper

) {

    private val executor = Executors.newWorkStealingPool(50)

    companion object {
        private val SS_ROOT_PATH = "http://3.uv128.com"
        //        private val MERCHANT_CODE = "xxxxxxxxxxx"
        private val log = LoggerFactory.getLogger(this::class.java)
    }


    fun <Req : SSReq, Resp : SSResp> post(path: String, req: Req, respClass: Class<Resp>): Resp {
        return post(path, req, respClass, { resp ->
            throw HanderSuccessfulCodeException("请求失败,返回的错误编码：[${resp.code}]")
        })
    }

    fun <Req : SSReq, Resp : SSResp> post(path: String, req: Req, respClass: Class<Resp>, body: (resp: Resp) -> Resp): Resp {

        val intactPath = SS_ROOT_PATH + path
        //log.info("请求地址：$intactPath, 请求数据：$req")

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON_UTF8

        val httpEntity: HttpEntity<Req> = HttpEntity(req, httpHeaders)


        val future = executor.submit(Callable<ResponseEntity<String>> {
            restTemplate.postForEntity(intactPath, httpEntity, String::class.java)
        })

        val respEntity: ResponseEntity<String> = future.get()

//        val respEntity = future.get() as ResponseEntity<*>

        if (respEntity.statusCode != HttpStatus.OK) {
            log.warn("请求返回的状态码错误,请求地址：$path 请求参数：$req")
            throw SSRequestHttpException("")
        }

        val respJson = respEntity.body as String
        log.info("请求地址：$intactPath 返回数据：$respJson")

        // json -> resp
        val resp = objectMapper.readValue(respJson, respClass)

        return if (resp.check()) {
            resp
        } else {
            body(resp)
        }
    }

}