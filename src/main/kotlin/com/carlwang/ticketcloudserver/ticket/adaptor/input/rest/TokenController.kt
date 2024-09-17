package com.carlwang.ticketcloudserver.ticket.adaptor.input.rest

import com.carlwang.ticketcloudserver.ticket.adaptor.input.rest.TicketController.HttpResp
import com.carlwang.ticketcloudserver.ticket.application.port.input.TokenInfo
import com.carlwang.ticketcloudserver.ticket.application.port.input.TokenUseCase
import com.carlwang.ticketcloudserver.ticket.config.TokenConfig
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/token")
@CrossOrigin
class TokenController(val tokenUseCase: TokenUseCase) {
    @PostMapping("/createClientToken")
    fun createTokens(@RequestBody tokenInfo: TokenInfo): TokenInfo {
        tokenUseCase.createToken(tokenInfo)
        return tokenInfo
    }

    @GetMapping("/check/{token}")
    fun checkToken(@PathVariable("token") token: String): TokenInfo {
        val tokenInfo = tokenUseCase.findByToken(token)
        return tokenInfo
    }
}