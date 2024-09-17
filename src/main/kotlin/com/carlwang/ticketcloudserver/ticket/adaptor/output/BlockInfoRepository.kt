package com.carlwang.ticketcloudserver.ticket.adaptor.output

import com.carlwang.ticketcloudserver.ticket.application.port.input.GeneralTicketUseCase
import com.carlwang.ticketcloudserver.ticket.application.port.output.BlockInfoDao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Created on 2024/3/1 14:35.
 * @author wangrui
 * @since 0.0.1
 */

@Repository
interface BlockInfoRepository : JpaRepository<BlockInfoDao.BlockInfoEntity, Long> {

    fun findByRoundId(scheduleId: Long): List<GeneralTicketUseCase.BlockInfoHistory> {
        return findByScheduleId(scheduleId)
            .groupBy { it.blockName }
            .map {
                val blockName = it.key
                val sumOfInventory = it.value.sumOf { it.inventory }
                GeneralTicketUseCase.BlockInfoHistory(blockName, sumOfInventory)
            }
    }

    fun findByScheduleId(scheduleId: Long): List<BlockInfoDao.BlockInfoEntity>


}