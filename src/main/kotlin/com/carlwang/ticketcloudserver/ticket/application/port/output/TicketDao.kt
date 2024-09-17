package com.carlwang.ticketcloudserver.ticket.application.port.output

import com.carlwang.ticketcloudserver.ticket.domain.Region

interface TicketDao {
    fun queryAllTicket(): Map<String, Region>

}