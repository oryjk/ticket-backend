package com.carlwang.ticketcloudserver.ticket.domain.service

import com.carlwang.ticketcloudserver.ticket.application.port.input.GeneralTicketUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.input.RequestUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.BlockInfoDao
import com.carlwang.ticketcloudserver.ticket.application.port.output.UserDao.Companion.autoUserInfoKey
import com.carlwang.ticketcloudserver.ticket.config.BaseUrlConfig
import com.carlwang.ticketcloudserver.ticket.config.GlobalConfig
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.client.RestTemplate
import javax.annotation.PostConstruct

/**
 * Created on 2024/2/28 23:37.
 * @author wangrui
 * @since 0.0.1
 */

@Service
class GeneralTicketService(
    val blockInfoDao: BlockInfoDao,
    val requestParamService: RequestParamService,
    val redisTemplate: RedisTemplate<String, Any>,
    @Value("\${autoCreateOrderWhenStartUp:false}") val autoCreateOrderWhenStartUp: Boolean,
) : GeneralTicketUseCase {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val restTemplate = RestTemplate()

    private val scope = CoroutineScope(Dispatchers.Default)


    @PostConstruct
    fun loop() {
        if (!autoCreateOrderWhenStartUp) {
            return
        }

        redisTemplate.delete(autoUserInfoKey)

        scope.launch {
            while (true) {
                val requestParam = requestParamService.getRobinOne()
                if (StringUtils.isEmpty(requestParam.useUrlLid2)) {
                    logger.info("当前没有用户，等待一下")
                    delay(1000)
                    continue
                }
                getLatestTicketInfo(
                    GlobalConfig.getUrl(
                        GlobalConfig.Api.GetBillRegion, BaseUrlConfig.getBaseUrl(), requestParam.useUrlLid2
                    ),
                    32, requestParam,
                )
                delay(1000)
            }

        }

    }

    override fun getLatestTicketInfo(
        url: String, scheduleId: Long, requestParam: RequestUseCase.Companion.RequestParam
    ): List<GeneralTicketUseCase.BlockInfo> {

        val requestBody = """
        {"id":"${requestParam.matchId}"}
    """.trimIndent()

        val requestEntity = HttpEntity<Any>(requestBody, requestParam.httpHeaders)

        val responseEntity = try {
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, TicketResponse::class.java)
        } catch (e: Exception) {
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, ErrorResponse::class.java)
        }
        when (val response = responseEntity.body) {
            is TicketResponse -> {
                return if ("立即购买" == response.btn_text) {

                    val data = response.data[0]
                    val blockInfos = data.list
                        .map {
                            GeneralTicketUseCase.BlockInfo(
                                scheduleId,
                                it.name,
                                it.usable_count,
                                it.price.toFloat()
                            )
                        }

                    GlobalScope.launch {
                        blockInfos.forEach {
                            blockInfoDao.saveOne(BlockInfoDao.BlockInfoEntity.toBlockInfoEntity(it))
                            logger.info("还有票，快抢 ${it.blockName}--${it.price}")

                        }
                    }
                    return blockInfos
                } else {
                    logger.info("没有票了 $response")
                    emptyList()
                }
            }

            is ErrorResponse -> {
                logger.error("Error: ${response.msg}")
                throw IllegalArgumentException()
            }

            else -> throw IllegalArgumentException()
        }


    }

    override fun getBlockInfo(scheduleId: Long): List<GeneralTicketUseCase.BlockInfoHistory> {
        return blockInfoDao.findByRoundId(scheduleId)
    }


    data class ErrorResponse(
        val code: Int,
        val msg: String,
    )

    data class TicketResponse(
        val btn_status: Int,
        val btn_text: String,
        val code: Int,
        val data: List<Data>,
        val msg: String,
        val max_num: Int,
        val type_code: Int
    )

    data class Data(
        val usable_count: Int,
        val max_price: String,
        val min_price: String,
        val region: Int,
        val list: List<ListItem>,
        val name: String
    )

    data class ListItem(
        val usable_count: Int, val price: String, val estate: Int, val name: String, val id: Int
    )


    data class MessageResponse(
        val code: Int,
        val data: ResponseData,
        val msg: String
    )

    data class ResponseData(
        val users: Int,
        val successSent: SuccessSent
    )

    data class SuccessSent(
        val mptext: Int,
        val sms: Int,
        val phonecall: Int
    )


}