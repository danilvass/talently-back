package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ConnectResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ConnectResponseStatus
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.UserCredentialsRepository.UserCredentialsRepository

import org.springframework.web.socket.WebSocketSession


class ConnectOperation(private val request:MobileRequest, private val session: WebSocketSession,
                       private val userCredentialsRepository: UserCredentialsRepository) : Runnable {


    override fun run() {
        synchronized(session) {
            when (userCredentialsRepository.checkIfUserTokenIsValid(request.userId, request.token)) {
                null -> session.sendResponse(MobileResponse(request.type, request.id, ConnectResponseData(ConnectResponseStatus.AUTH_REQUIRED)))
                else -> session.sendResponse(MobileResponse(request.type, request.id, ConnectResponseData(ConnectResponseStatus.SUCCESS)))
            }
        }
    }


}
