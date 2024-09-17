package com.carlwang.ticketcloudserver.ticket.adaptor.input.endpoint

import com.carlwang.ticketcloudserver.ticket.domain.service.OrderService
import com.carlwang.ticketcloudserver.ticket.application.port.input.SignUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.AutoBuyDao
import com.carlwang.ticketcloudserver.ticket.application.port.output.MatchReader
import com.carlwang.ticketcloudserver.ticket.application.port.output.TicketDao
import com.carlwang.ticketcloudserver.ticket.application.port.output.UserInfoDao
import org.springframework.stereotype.Service

@Service
class OrderEndpoint(
    val ticketDao: TicketDao,
    val autoBuyDao: AutoBuyDao,
    val orderService: OrderService,
    val matchReader: MatchReader,
    val userInfoDao: UserInfoDao,
    val signUseCase: SignUseCase
) {


}