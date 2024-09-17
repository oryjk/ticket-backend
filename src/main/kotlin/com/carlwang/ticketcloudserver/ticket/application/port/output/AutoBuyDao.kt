package com.carlwang.ticketcloudserver.ticket.application.port.output

import com.carlwang.ticketcloudserver.ticket.adaptor.output.OrderRequestEntity
import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderTaskUseCase

/**
 * 自动下单的信息，返回的是需要自动下单的用户和他的选区
 */
interface AutoBuyDao {
    fun getAutoBuyInfo(): List<OrderTaskUseCase.OrderRequest>
    fun save(orderRequestEntity: OrderRequestEntity)

}