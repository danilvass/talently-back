package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.data.SignUpData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.SignUpResponseErrorType
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.UserCredentialsRepository.UserCredentialsRepository
import com.tripled.talentlyapp.utils.createEnumFrom
import org.apache.ibatis.exceptions.PersistenceException
import org.springframework.web.socket.WebSocketSession
import java.sql.SQLException

class SignUpOperation(private val requestType: MobileRequestType, private val requestId:String, private val data: SignUpData,
                      private val session: WebSocketSession, private val userCredentialsRepository: UserCredentialsRepository) : Runnable {

    override fun run() {
        synchronized(session) {
            try {
                val signUpData = userCredentialsRepository.signUp(data.login, data.password)
                session.sendResponse(MobileResponse(requestType,requestId, signUpData))
            } catch (e: PersistenceException) {
                when (createEnumFrom<SignUpResponseErrorType>((e.cause?.message.toString()))) {
                    SignUpResponseErrorType.LOGIN_DUPLICATE -> {
                        session.sendResponse(MobileResponse(requestType,requestId, null,
                                ErrorResponseData(ErrorType.BUSINESS_ERROR, SignUpResponseErrorType.LOGIN_DUPLICATE.toString(), "Login is duplicated")))
                    }
                    null ->
                        session.sendResponse(MobileResponse(requestType,requestId, null,
                                ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), "Server Error Occured")))

                }
            } catch (e: SQLException) {
                session.sendResponse(MobileResponse(requestType,requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }
        }


    }

}
