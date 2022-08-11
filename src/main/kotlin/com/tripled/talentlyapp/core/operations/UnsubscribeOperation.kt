package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.data.UnsubscribeRequestData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.SubscribeRepository.ISubscribeRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class UnsubscribeOperation (private val requestType: MobileRequestType, private val requestId: String, private val userId: String,
                            private val unsubscribeData: UnsubscribeRequestData, private val session: WebSocketSession,
                            private val subscribeRepository: ISubscribeRepository) : Runnable {
    override fun run() {
        synchronized(session) {
            try {
                subscribeRepository.unsubscribe(userId,unsubscribeData.portfolioId)
                session.sendResponse(MobileResponse(requestType, requestId, SuccessResponse()))
            } catch (ex: Exception) {
                session.sendResponse(MobileResponse(requestType, requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, ex.stackTrace.toString(), "Server Error Occured")))
            }
        }
    }


}