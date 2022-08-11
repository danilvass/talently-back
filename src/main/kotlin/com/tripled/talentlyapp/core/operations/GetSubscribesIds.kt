package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.SubscribeIds
import com.tripled.talentlyapp.database.repositories.SubscribeRepository.ISubscribeRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class GetSubscribesIds(private val request: MobileRequest, private val session: WebSocketSession,
                       private val subscribeRepository: ISubscribeRepository) : Runnable {
    override fun run() {
        synchronized(session) {
            try {
                val subscriptions = subscribeRepository.getSubscribesIds(request.userId)
                if (subscriptions == null) {
                    session.sendResponse(MobileResponse(request.type, request.id, SuccessResponse()))
                } else {
                    session.sendResponse(MobileResponse(request.type, request.id, SubscribeIds(subscriptions)))
                }
            } catch (ex: Exception) {
                session.sendResponse(MobileResponse(request.type, request.id, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, ex.stackTrace.toString(), "Server Error Occured")))
            }
        }
    }


}