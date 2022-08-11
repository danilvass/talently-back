package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.Portfolios
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.database.repositories.PortfolioRepository.IPortfolioRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class SearchPortfolioOperation(private val request: MobileRequest, private val session: WebSocketSession,
                               private val portfolioRepository: IPortfolioRepository, private val search: String?) : Runnable {

    override fun run() {
        synchronized(session) {
            try {
                if (search == "" || search == null){
                    session.sendResponse(MobileResponse(request.type, request.id, Portfolios(arrayListOf())))
                }
                else {
                    val data = Portfolios(portfolioRepository.searchPortfilio(search))
                    session.sendResponse(MobileResponse(request.type, request.id, data))
                }
            } catch (e: Exception) {
                session.sendResponse(MobileResponse(request.type, request.id, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }

        }
    }

}