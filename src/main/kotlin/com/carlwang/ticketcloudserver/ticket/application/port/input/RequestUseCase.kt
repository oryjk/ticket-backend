package com.carlwang.ticketcloudserver.ticket.application.port.input

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestParam

interface RequestUseCase {

    fun getByUserId(userId: String): RequestParam

    fun getHeader(token: String, hostName: String): HttpHeaders
    fun getRobinOne(): RequestParam

    companion object {
        data class RequestParam(
            val useUrlLid2: String,
            val matchId: String,
            val token: String,
            val httpHeaders: HttpHeaders
        ) {
            companion object {
                fun default(): RequestParam {
                    return RequestParam("", "", "", HttpHeaders.EMPTY)
                }
            }

        }


    }
}