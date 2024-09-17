package com.carlwang.ticketcloudserver.ticket.application

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.carlwang.ticketcloudserver.ticket.domain.service.GeneralTicketService
import com.carlwang.ticketcloudserver.ticket.domain.service.TicketInMemoryHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class GeneralTicketServiceTest {


    @Test
    fun sortTicketResponseTest() {
        val ticketResponse =
            jacksonObjectMapper().readValue<GeneralTicketService.TicketResponse>(TicketInMemoryHelper.responseJson)
        ticketResponse.data.flatMap { it.list }.sortedByDescending { it.usable_count }
            .forEach { println("区域 ${it.name} 的余票数量为 ${it.usable_count} 价格为 ${it.price}") }
    }

    /**
     * f = e.seKey + l + e.loginCode + u
     * h.hex_md5_32(f)
     */
    @Test
    fun validate() {
//        val seKey = "r1b07Nym"//u1RPHY5C,Y6xkC7b5
//        val seKey = "u1RPHY5C"//u1RPHY5C,Y6xkC7b5
        val seKey = "Y6xkC7b5"//u1RPHY5C,Y6xkC7b5
        val s_time = "1716643896"// l 下单的时候的时间戳
        val loginCode = "0a3Xeill2AKWvd43hBol2BVEtx1XeilL" // 微信用户的code，每次获取都会让上一次失效，5分钟过期
        val u = "432555" //用户的id

        val hexMd532 = getSSign(seKey, s_time, loginCode, u)

        println(hexMd532)
        Assertions.assertEquals("84736a6c9d451485e6e5e338d938b2e1", hexMd532)
    }

    @Test
    fun validate2() {
//        val seKey = "r1b07Nym"//u1RPHY5C,Y6xkC7b5
//        val seKey = "u1RPHY5C"//u1RPHY5C,Y6xkC7b5
        val seKey = "Y6xkC7b5"//u1RPHY5C,Y6xkC7b5
        val s_time = "1714902681"// l 下单的时候的时间戳
        val loginCode = "0c3sqrFa1XFcoH0230Ja1kGwU60sqrFQ" // 微信用户的code，每次获取都会让上一次失效，5分钟过期，但是可以一直使用
        val u = "432555" //用户的id

        val hexMd532 = getSSign(seKey, s_time, loginCode, u)

        println(hexMd532)
        Assertions.assertEquals("c4dacf90b11041ec4bde9ba1cbed7dfc", hexMd532)
    }
    @Test
    fun validate3() {
        val seKey = "Y6xkC7b5"//u1RPHY5C,Y6xkC7b5
        val s_time = "1719648613"// l 下单的时候的时间戳
        val loginCode = "0c1Ihq000jg4nS1oq4100NyKEb3Ihq0A" // 微信用户的code，每次获取都会让上一次失效，5分钟过期，但是可以一直使用
        val u = "116692" //用户的id

        val hexMd532 = getSSign(seKey, s_time, loginCode, u)

        println(hexMd532)
        Assertions.assertEquals("d6ff0ba277d761ebe9923a208336398e", hexMd532)
    }


    fun getSSign(seKey: String, s_time: String, loginCode: String, u: String): String {
        val f = seKey + s_time + loginCode + u
        val hexMd532 = hex_md5_32(f)
        return hexMd532
    }

    fun hex_md5_32(input: String): String {
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

    @Test
    fun changeFileName() {
        val dirPath = "E:/抢票/武汉/fccdn1.k4n.cc/fc/wx_api/v1"
        val dir = File(dirPath)

        changeName(dir)
    }

    private fun changeName(dir: File) {
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.forEach { file ->
                if (file.isFile) {
                    val newFile = File(file.absolutePath + ".json")
                    file.renameTo(newFile)
                } else {
                    changeName(file)
                }
            }
        }
    }
}