package com.carlwang.ticketcloudserver.ticket.adaptor.output

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.carlwang.ticketcloudserver.ticket.domain.service.GeneralTicketService
import com.carlwang.ticketcloudserver.ticket.domain.service.TicketInMemoryHelper
import com.carlwang.ticketcloudserver.ticket.domain.Region
import com.carlwang.ticketcloudserver.ticket.application.port.output.TicketDao
import org.springframework.stereotype.Service

@Service
class TicketInMemory : TicketDao {

    private val allTickets =
        jacksonObjectMapper().readValue<GeneralTicketService.TicketResponse>(TicketInMemoryHelper.responseJson).data.flatMap {
            it.list.map { item ->
                Region(
                    it.region, item.estate, 1, item.name, item.price, item.usable_count.toString()
                )
            }
        }.associateBy { it.name }


    override fun queryAllTicket(): Map<String, Region> {
        return allTickets
    }
}