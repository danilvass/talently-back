package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.data.SubscribeRequestData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.SubscribeRepository.ISubscribeRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class SubscribeOperation(private val requestType: MobileRequestType, private val requestId: String, private val userId: String,
                         private val subscribeData: SubscribeRequestData, private val session: WebSocketSession,
                         private val subscribeRepository: ISubscribeRepository) : Runnable {
    override fun run() {
        synchronized(session) {
            try {

                subscribeRepository.addPortfolioSubscription(userId, subscribeData.portfolioId)
                session.sendResponse(MobileResponse(requestType, requestId, SuccessResponse()))

            } catch (ex: Exception) {
                session.sendResponse(MobileResponse(requestType, requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, ex.stackTrace.toString(), "Server Error Occured")))
            }
        }
    }


}