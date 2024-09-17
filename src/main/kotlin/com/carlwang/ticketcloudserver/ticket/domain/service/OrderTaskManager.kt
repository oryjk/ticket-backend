package com.carlwang.ticketcloudserver.ticket.domain.service

import com.carlwang.ticketcloudserver.extensions.slf4k
import com.carlwang.ticketcloudserver.ticket.application.port.input.GeneralTicketUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderTaskUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.input.SignUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.BlockInfoDao
import com.carlwang.ticketcloudserver.ticket.application.port.output.TicketDao
import com.carlwang.ticketcloudserver.ticket.domain.*
import com.carlwang.ticketcloudserver.utils.EmailSender
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@Service
class OrderTaskManager(
    val signUseCase: SignUseCase, ticketDao: TicketDao, val orderUseCase: OrderUseCase, val blockInfoDao: BlockInfoDao
) : OrderTaskUseCase {
    private val logger = slf4k()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val allRegion = ticketDao.queryAllTicket()

    private val regex = "\\d+"
    private val pattern = Pattern.compile(regex)
    private val restTemplate = RestTemplate()

    private val statusMsgPrefix = mapOf<Int, String>(
        0 to "~~~~~~", 1 to "√√√√√√", 2 to "√√++++", 3 to "⭕⭕⭕⭕", 5 to "××××××"
    )


    override fun createOrderJob(jobId: String, orderRequest: OrderTaskUseCase.OrderRequest): Job {

        return scope.launch {
            var count = 0
            while (true) {
                try {
                    count++
                    val result = sendOrder(orderRequest)
                    logInConsole(orderRequest, result, count)
                    if (count == 10) {
                        count = 0
                    }
                } catch (e: Exception) {
                    logger.error(e.message)
                    delay(10000)
                }
//                yield()
            }
        }

    }

    private fun logInConsole(
        orderRequest: OrderTaskUseCase.OrderRequest, result: CreateMatchOrderResponse, count: Int
    ) {
        var regions = regions(orderRequest)
        val nameStr = orderRequest.users.map { it.realName }.reduce { acc, i -> "$acc , $i" }
        val regionStr = regions.map { it.name }.reduce { acc, i -> "$acc , $i" }
        val regionPriceStr = regions.map { it.price }.reduce { acc, i -> "$acc , $i" }


        when (result.code) {
            -1 -> {
                logger.error("${orderRequest.users[0].realName}|${orderRequest.regions[0]}|登录信息失效 ${result.msg}")
                EmailSender.sendEmail(
                    listOf(
//                        "331675560@qq.com",
                        "oryjk@qq.com"
                    ),
                    "成都蓉城抢票失败token过期 ${orderRequest.users[0].realName}|${orderRequest.regions[0]}",
                    "名字 $nameStr, 区域 $regionStr，当前时间 ${LocalDateTime.now()}，${orderRequest.users[0].realName}|${orderRequest.regions[0]} 过期"
                )
                TimeUnit.SECONDS.sleep(300)
            }

            0 -> {
                if (count == 10) {
                    logger.info("${statusMsgPrefix[0]} ${orderRequest.orderId}: 【$nameStr】当前抢的 【$regionStr】 区，价格 【$regionPriceStr】，状态码是${result.code}，${result.msg}")
                }
                val matcher = pattern.matcher(result.msg)
                if (matcher.find()) {
                    val number = matcher.group()
                    TimeUnit.SECONDS.sleep(number.toLong())
                } else {
                    throw IllegalArgumentException("状态是5，但是没有超时时间，很奇怪哦")
                }

            }

            1 -> {
                logger.info("${statusMsgPrefix[1]} ${orderRequest.orderId}: 【$nameStr】当前抢的 【$regionStr】 区，价格 【$regionPriceStr】，状态码是${result.code}，${result.msg} √√√√√ √√√√√ √√√√√ √√√√√ √√√√√ √√√√√ √√√√√")
                logger.info("~~~~~~~~~~~~~~~恭喜，恭喜~~~~~发送瞄提醒~~~~~~~~~~")

                try {
                    restTemplate.getForObject(
                        "http://miaotixing.com/trigger?id=t5i54aH&type=json", String::class.java
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                EmailSender.sendEmail(
                    listOf(
//                        "331675560@qq.com",
                        "oryjk@qq.com"
                    ),
                    "成都蓉城抢票成功",
                    "名字 $nameStr, 区域 $regionStr，当前时间 ${LocalDateTime.now()}，过期时间 ${
                        LocalDateTime.now().plusMinutes(15)
                    }"
                )

                regions.forEach {
                    blockInfoDao.saveOne(
                        BlockInfoDao.BlockInfoEntity.toBlockInfoEntity(
                            GeneralTicketUseCase.BlockInfo(
                                orderRequest.matchId.toLong(), it.region.toString(), 1, it.price.toFloat()
                            )
                        )
                    )
                }


            }

            2 -> {
                logger.info("${statusMsgPrefix[2]} ${orderRequest.orderId}: 【$nameStr】当前抢的 【$regionStr】 区，价格 【$regionPriceStr】，状态码是${result.code}，${result.msg}√√√√√ √√√√√ √√√√√ √√√√√ 已经抢到，有待支付订单")

            }

            3 -> {
                logger.info("${statusMsgPrefix[3]} ${orderRequest.orderId}: 【$nameStr】当前抢的 【$regionStr】 区，价格 【$regionPriceStr】，状态码是${result.code}，${result.msg}")

            }

            5 -> {
                if (count == 10) {
                    logger.info("${statusMsgPrefix[5]} ${orderRequest.orderId}: 【$nameStr】当前抢的 【$regionStr】 区，价格 【$regionPriceStr】，状态码是${result.code}，${result.msg} 休眠 ${result.timeout} 秒")
                }
                TimeUnit.SECONDS.sleep(result.timeout)
            }

            else -> {
                throw IllegalArgumentException("${orderRequest.orderId}: 状态是${result.code}，没有遇到过这个code")
            }
        }
    }

    private fun regions(orderRequest: OrderTaskUseCase.OrderRequest): List<Region> {
        var regions = orderRequest.regions.map { allRegion[it]!! }
        if (regions.size != orderRequest.users.size) {
            val region = regions[0]
            regions = listOf(
                Region(
                    region.region,
                    region.estate,
                    orderRequest.users.size,
                    region.name,
                    region.price,
                    region.usable_count
                )
            )
        }
        return regions
    }

    private fun sendOrder(orderRequest: OrderTaskUseCase.OrderRequest): CreateMatchOrderResponse {
        val now = LocalDateTime.now()
        val epochSecond = now.atZone(ZoneId.systemDefault()).toEpochSecond()
        val uId = orderRequest.users[0].uid.toString()
        val sign = signUseCase.getSign("Y6xkC7b5", epochSecond, orderRequest.loginCode, uId)

        val createOrderQueryParam = CreateOrderQueryParam(uId, epochSecond, sign, orderRequest.token)
        var regions = regions(orderRequest)

        val createMatchOderRequest = CreateMatchOderRequest(orderRequest.encryptedData,
            orderRequest.version,
            orderRequest.expireTime,
            true,
            orderRequest.matchId,
            regions,
            orderRequest.users.map {
                User.defaultUser(
                    it.id, it.uid, it.timestamp, it.signature, it.realName, it.realCardId2, it.phone2
                )
            })
        val createOrderParam = CreateOrderParam(createOrderQueryParam, createMatchOderRequest)
        return orderUseCase.sendOrder(createOrderParam)
    }

}