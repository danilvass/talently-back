package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.requests.data.SignInRequestData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.CheckUserCredentialsResult
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.UserCredentialsRepository.UserCredentialsRepository
import com.tripled.talentlyapp.utils.createEnumFrom
import org.apache.ibatis.exceptions.PersistenceException
import org.springframework.web.socket.WebSocketSession
import java.sql.SQLException


class SignInOperation(private val requestType: MobileRequestType,private val requestId:String,private val signInData: SignInRequestData,
                      private val session: WebSocketSession, private val userCredentialsRepository: UserCredentialsRepository) : Runnable {

    override fun run() {
        synchronized(session) {
            try {
                val checkUserCredentialsResultData = userCredentialsRepository.checkUserCredentials(signInData.login, signInData.password)
                session.sendResponse(MobileResponse(requestType, requestId, checkUserCredentialsResultData))
            } catch (e: PersistenceException) {
                when (createEnumFrom<CheckUserCredentialsResult>((e.cause?.message.toString()))) {
                    CheckUserCredentialsResult.INCORRECT_LOGIN ->
                        session.sendResponse(MobileResponse(requestType, requestId, null,
                                ErrorResponseData(ErrorType.BUSINESS_ERROR, CheckUserCredentialsResult.INCORRECT_LOGIN.toString(), "User with that login doesn't exist")))
                    CheckUserCredentialsResult.INCORRECT_PASSWORD ->
                        session.sendResponse(MobileResponse(requestType, requestId, null,
                                ErrorResponseData(ErrorType.BUSINESS_ERROR, CheckUserCredentialsResult.INCORRECT_PASSWORD.toString(), "Incorrect Password")))
                    null ->
                        session.sendResponse(MobileResponse(requestType, requestId, null,
                                ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), "Server Error Occured")))
                }


            } catch (e: SQLException) {
                session.sendResponse(MobileResponse(requestType, requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }
        }


    }

}


