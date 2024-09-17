package com.carlwang.ticketcloudserver.ticket.domain.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.carlwang.ticketcloudserver.extensions.slf4k
import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.input.RequestUseCase
import com.carlwang.ticketcloudserver.ticket.domain.CreateMatchOrderResponse
import com.carlwang.ticketcloudserver.ticket.domain.CreateMatchOrderUrl
import com.carlwang.ticketcloudserver.ticket.domain.CreateOrderParam
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OrderService(
    val requestUseCase: RequestUseCase
) : OrderUseCase {

    private val restTemplate = RestTemplate()
    private val logger = slf4k()

    override fun sendOrder(requestParam: CreateOrderParam): CreateMatchOrderResponse {

        val queryParam = requestParam.queryParam
        val createMatchOrderUrl = CreateMatchOrderUrl(
            queryParam.userId, queryParam.timeStamp.toString(), queryParam.sign
        )
        val host = createMatchOrderUrl.host()
        val requestEntity = HttpEntity<Any>(
            jacksonObjectMapper().writeValueAsString(requestParam.requestBody),
            requestUseCase.getHeader(queryParam.token, host)
        )

        val url = createMatchOrderUrl.toString()
        logger.trace("url {}, body {}", url, requestEntity)
        val responseEntity =
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, CreateMatchOrderResponse::class.java)
        return responseEntity.body!!
    }
}