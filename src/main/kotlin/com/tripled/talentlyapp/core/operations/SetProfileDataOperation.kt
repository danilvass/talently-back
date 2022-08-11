package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.data.SetProfileRequestData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.ProfileRepository.IProfileRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class SetProfileDataOperation(private val requestType: MobileRequestType, private val requestId: String,
                              private val userId: String, private val setData: SetProfileRequestData,
                              private val session: WebSocketSession, private val profileRepository: IProfileRepository) : Runnable {
    override fun run() {
        synchronized(session) {
            try {
                profileRepository.setProfileDataByUserId(userId,setData.gender,setData.age,setData.description)
                session.sendResponse(MobileResponse(requestType, requestId, SuccessResponse()))
            } catch (e: Exception) {
                session.sendResponse(MobileResponse(requestType, requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }
        }
    }

}