package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.UserCredentialsRepository.UserCredentialsRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class SignOutOperation(private val request: MobileRequest, private val session: WebSocketSession,
                       private val userCredentialsRepository: UserCredentialsRepository) : Runnable {

    override fun run() {
        synchronized(session) {
            try {

                userCredentialsRepository.signOut(request.token, request.userId)
                session.sendResponse(MobileResponse(request.type, request.id, SuccessResponse()))

            } catch (ex: Exception) {
                session.sendResponse(MobileResponse(request.type, request.id, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, ex.stackTrace.toString(), "Server Error Occured")))
            }
        }
    }
}