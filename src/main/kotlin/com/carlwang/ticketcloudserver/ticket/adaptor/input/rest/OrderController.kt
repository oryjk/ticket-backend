package com.carlwang.ticketcloudserver.ticket.adaptor.input.rest

import com.carlwang.ticketcloudserver.extensions.slf4k
import com.carlwang.ticketcloudserver.ticket.adaptor.output.OrderRequestEntity
import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderTaskUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.AutoBuyDao
import com.carlwang.ticketcloudserver.ticket.domain.service.OrderTaskManager
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/order")
@CrossOrigin
class OrderController(
   val orderTaskManager: OrderTaskManager, val autoBuyDao: AutoBuyDao
) {

    private val logger = slf4k()


    @PostMapping("/createSimpleOrder")
    fun createSimpleOrder(@RequestBody clientOrderRequest: ClientOrderRequest): String {
        val orderRequestEntity = OrderRequestEntity(
            clientOrderRequest.orderId,
            clientOrderRequest.matchId,
            clientOrderRequest.orderPayload,
            clientOrderRequest.loginCode,
            clientOrderRequest.token,
            LocalDateTime.now(),
            clientOrderRequest.clientTokenId
        )
        autoBuyDao.save(orderRequestEntity)
        logger.info("${clientOrderRequest.orderId} 接收到新的订单，已经保存到DB, 客户端推荐码为：${clientOrderRequest.clientTokenId}")
        val result = clientOrderRequest.clientTokenId + clientOrderRequest.orderId
        return result
    }

//    @GetMapping("/autoCreate")
//    fun createAutoOrder(): List<String> {
//        val orderRequests = autoBuyDao.getAutoBuyInfo()
//        val jobIds = orderRequests.map { orderRequest ->
//            orderTaskUseCase.createOrderJob(orderRequest.orderId, orderRequest)
//        }
//        return jobIds
//    }

//    @GetMapping("/stop/{id}")
//    fun stopAutoOrder(@PathVariable("id") id: String) {
//        orderTaskManager.cancelOrderJob(id)
//    }


    data class ClientOrderRequest(
        val orderId: String,
        val matchId: String,
        val orderPayload: String,
        val loginCode: String,
        val token: String,
        val clientTokenId: String
    )

}