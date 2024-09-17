package com.carlwang.ticketcloudserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketBackendApplication

fun main(args: Array<String>) {
    runApplication<TicketBackendApplication>(*args)
}
