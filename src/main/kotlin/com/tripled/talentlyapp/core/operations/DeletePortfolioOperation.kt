package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.data.DeletePortfolioRequestData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.PortfolioRepository.IPortfolioRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class DeletePortfolioOperation(private val requestType: MobileRequestType, private val requestId: String,
                               private val deletePortfolioData: DeletePortfolioRequestData, private val session: WebSocketSession,
                               private val portfolioRepository: IPortfolioRepository):Runnable  {
    override fun run() {
        synchronized(session) {
            try {
                portfolioRepository.deletePortfolio(deletePortfolioData.portfolioId)
                session.sendResponse(MobileResponse(requestType, requestId, SuccessResponse()))
            } catch (e: Exception) {
                session.sendResponse(MobileResponse(requestType, requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }
        }
    }
}