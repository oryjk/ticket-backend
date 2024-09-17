package com.carlwang.ticketcloudserver.ticket.application.port.input

interface SignUseCase {
    fun getSign(seKey: String, timeStamp: Long, loginCode: String, userId: String): String
}