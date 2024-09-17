package com.carlwang.ticketcloudserver.ticket.domain

import com.carlwang.ticketcloudserver.configuration.RedisConfig
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Created on 2024/9/11 16:29.
 * @author wangrui
 * @since 0.0.1
 */

data class OrderPayloadRoot(
    val encryptedData: String,
    val version: Int,
    val expireTime: Long,
    val agree: Boolean,
    val id: String,
    val regions: List<Region>,
    val users: List<User>
) {
    companion object {

        fun convert(json: String): OrderPayloadRoot {
            val root = RedisConfig.objectMapper.readValue<OrderPayloadRoot>(json)
            return root
        }


        data class Region(
            val region: Int, val estate: Int, val num: Int, val name: String, val price: String, val usable_count: Int
        )

        data class User(
            val id: Int,
            val uid: Int,
            val realname: String,
            val real_card_id: String,
            val phone: String,
            val is_self: Boolean,
            val real_card_id2: String,
            val phone2: String,
            val timestamp: Long,
            val signature: String,
            val disabled: Boolean,
            val disabled2: Boolean,
            val showText: String
        )
    }
}


