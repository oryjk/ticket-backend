package com.carlwang.ticketcloudserver.ticket.adaptor.input

import com.carlwang.ticketcloudserver.extensions.slf4k
import com.carlwang.ticketcloudserver.ticket.adaptor.output.OrderRequestEntity
import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderTaskUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.AutoBuyDao
import com.carlwang.ticketcloudserver.ticket.application.port.output.UserDao
import com.carlwang.ticketcloudserver.ticket.application.port.output.UserDao.Companion.autoUserInfoKey
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class OrderListener(
    val autoBuyDao: AutoBuyDao,
    val orderTaskUseCase: OrderTaskUseCase,
    val redisTemplate: RedisTemplate<String, Any>,
    @Value("\${autoCreateOrderWhenStartUp:false}") val autoCreateOrderWhenStartUp: Boolean,
    @Value("\${jsonFilePath:E:\\抢票\\spider_img}") val jsonFilePath: String
) {
    private val logger = slf4k()
    private val srcPath = Paths.get(jsonFilePath) // 源文件夹路径
    private val INDEX_FILE = "index.json"
    private val REQUEST_FILE = "request.json"
    private val AUTH_FILE = "auth.json"
    private val scope = CoroutineScope(Dispatchers.Default)
    private val clientToken = "itX3oz0BsBxC0XtbuTOrgSK3iidWu7hp"

    private val jobMapping = mutableMapOf<String, Job>()

    @PostConstruct
    fun ssss() {
//        while (true) {
//            val now = LocalTime.now()
//            val targetTime = LocalTime.of(14, 0, 0) // 14:00:00
//
//            if (now.isAfter(targetTime)) {
//                logger.info("开始战斗！！！！！！！！！")
//                loopOrderRequest()
//                return
//            } else {
//                logger.info("还没到14点0分0秒  $now")
//            }
//
//            Thread.sleep(1000) // 休眠1秒
//        }
        if (autoCreateOrderWhenStartUp) {
            loopOrderRequest()

        }

    }

    fun cancelOrderJob(jobId: String) {
        logger.warn("主要，马上取消任务 $jobId!!!")
        jobMapping[jobId]?.cancel()
    }

    fun loopOrderRequest() {
        scope.launch {
            while (true) {
                createOrders()
                TimeUnit.SECONDS.sleep(1)
            }
        }

    }

    private fun processFilesIfExists(files: List<Path>): String {
        var orderId = ""
        val matchId: String

        if (files.all { Files.exists(it) }) {
            logger.info("All files exist, processing starts.")

            val codeFile = files[0]
            val orderPayloadFile = files[1]
            val tokenFile = files[2]

            val loginCode = extractCodeFromJson(codeFile)
            val orderPayload = fetchContentFromFile(orderPayloadFile)

            val jsonElement = Json.parseToJsonElement(orderPayload)
            if (jsonElement !is JsonObject) return ""
            matchId = extractMatchIdFromOrderPayload(jsonElement)
            orderId = extractIdentifierFromOrderPayload(jsonElement)

            val token = fetchContentFromFile(tokenFile)

            val orderRequestEntity = OrderRequestEntity(
                orderId, matchId, orderPayload, loginCode, token, LocalDateTime.now(), clientToken
            )
            autoBuyDao.save(orderRequestEntity)
            moveFileToProcessingDir(orderPayloadFile, orderId)
        } else {
            logger.info("Some files are missing, waiting for them.")
        }

        return orderId
    }

    private fun createOrders() {
        val orderRequests = autoBuyDao.getAutoBuyInfo()
        orderRequests.filter { !ObjectUtils.isEmpty(it.matchId) && !jobMapping.contains(it.orderId) }
            .forEach { orderRequest ->

                logger.info("创建任务，任务id ${orderRequest.orderId}，当前时间 ${LocalDate.now()}")
                val createOrderJob = orderTaskUseCase.createOrderJob(orderRequest.orderId, orderRequest)
                jobMapping[orderRequest.orderId] = createOrderJob

                redisTemplate.boundSetOps(autoUserInfoKey)
                val miniUserInfo = UserDao.Companion.MiniUserInfo(
                    orderRequest.users[0].uid.toString(),
                    orderRequest.token,
                    orderRequest.version,
                    orderRequest.users[0].realName,
                    "https://servicewechat.com/wxffa42ecd6c0e693d/70/page-frame.html"
                )
                redisTemplate.opsForSet().add(autoUserInfoKey, miniUserInfo)


            }

    }

    private fun extractMatchIdFromOrderPayload(jsonElement: JsonObject): String =
        jsonElement["id"]?.jsonPrimitive?.content!!


    private fun extractCodeFromJson(codeFile: Path): String {
        val content = fetchContentFromFile(codeFile)
        val jsonElement = Json.parseToJsonElement(content)
        if (jsonElement !is JsonObject || jsonElement["code"]?.jsonPrimitive?.content == null) {
            throw IllegalArgumentException("index.json content cannot be parsed into code.")
        }

        return jsonElement["code"]?.jsonPrimitive?.content!!
    }

    private fun fetchContentFromFile(file: Path): String {
        val content = Files.readString(file)
        logger.info(content)

        return content
    }

    private fun extractIdentifierFromOrderPayload(jsonElement: JsonObject): String {
        val region = jsonElement["regions"]?.jsonArray?.get(0)
        val user = jsonElement["users"]?.jsonArray?.get(0)
        if (region !is JsonObject || user !is JsonObject) return ""

        val regionName = region["name"]?.jsonPrimitive?.content
        val userName = user["realname"]?.jsonPrimitive?.content
        logger.info("$regionName $userName")

        return "$userName$regionName"
    }

    private fun moveFileToProcessingDir(orderPayloadFile: Path, id: String) {
        val destPath = Paths.get("E:\\抢票\\spider_img\\processed\\$id") // replace this with constant
        if (Files.notExists(destPath)) {
            Files.createDirectories(destPath)
        }

        val destinationFile = destPath.resolve(orderPayloadFile.fileName.toString())
        Files.move(orderPayloadFile, destinationFile, StandardCopyOption.REPLACE_EXISTING)
        logger.info("All files processed.")
    }
}
