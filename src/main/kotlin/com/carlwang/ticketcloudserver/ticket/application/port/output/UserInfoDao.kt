package com.carlwang.ticketcloudserver.ticket.application.port.output

import com.carlwang.ticketcloudserver.ticket.domain.User

interface UserInfoDao {


    fun queryById(userId: String): User


}