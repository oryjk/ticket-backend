package com.carlwang.ticketcloudserver.ticket.domain.service

import com.carlwang.ticketcloudserver.extensions.slf4k
import com.carlwang.ticketcloudserver.ticket.application.port.input.SignUseCase
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest

@Service
class SignService : SignUseCase {

    val logger = slf4k()

    /**
     * @param seKey 盐，起到盐的作用，现在是配置在小程序前端代码的，Y6xkC7b5
     * @param timeStamp 提交订单时候的时间戳
     * @param loginCode 用户登录小程序的官方code，这个code虽然5分钟有效，但是一旦兑换成功，就可以使用直到token过期
     * @param userId 用户的id，table rs_mini_user 的 id 字段
     */
    override fun getSign(seKey: String, timeStamp: Long, loginCode: String, userId: String): String {
        val f = seKey + timeStamp + loginCode + userId
        return hexMd532(f).also { logger.trace("timeStamp is $timeStamp, loginCode is $loginCode, userId is $userId, md532 result is $it") }
    }

    private fun hexMd532(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val byteArray = md.digest(input.toByteArray())
        val bigInt = BigInteger(1, byteArray)
        var hashText = bigInt.toString(16)
        // Pad with leading zeros to ensure the length is 32
        while (hashText.length < 32) {
            hashText = "0$hashText"
        }
        return hashText
    }
}