package com.carlwang.ticketcloudserver.ticket.adaptor.output

import com.carlwang.ticketcloudserver.ticket.application.port.input.OrderTaskUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.AutoBuyDao
import com.carlwang.ticketcloudserver.ticket.domain.OrderPayloadRoot
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Created on 2024/5/7 20:06.
 * @author wangrui
 * @since 0.0.1
 */

@Repository
interface AutoBuyRepository : JpaRepository<OrderRequestEntity, String>, AutoBuyDao {

    override fun getAutoBuyInfo(): List<OrderTaskUseCase.OrderRequest> {
        return findAll().map {
            val root = OrderPayloadRoot.convert(it.orderPayload)

            OrderTaskUseCase.OrderRequest(
                it.id,
                root.encryptedData,
                root.version,
                root.users.map { user ->
                    OrderTaskUseCase.UserOrderInfo(
                        user.id,
                        user.uid,
                        user.signature,
                        user.timestamp,
                        user.realname,
                        user.real_card_id2,
                        user.phone2
                    )
                },
                it.loginCode,
                root.regions.map { it.name },
                root.expireTime,
                it.token,
                root.id,
                it.tokenId
            )
        }
    }
}

@Table(name = "rs_order_request")
@Entity
class OrderRequestEntity(
    @Id val id: String,
    val matchId: String,
    @Column(columnDefinition = "TEXT") val orderPayload: String,
    val loginCode: String,
    val token: String,
    val time: LocalDateTime,
    val tokenId: String
)


