package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.data.GetProfileRequestData
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SuccessResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.database.repositories.ProfileRepository.IProfileRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class GetProfileDataOperation(private val requestId:String,private val requestType: MobileRequestType,
                              private val getProfileData:GetProfileRequestData,private val session: WebSocketSession,
                              private val profileRepository: IProfileRepository) : Runnable {

    override fun run() {
        synchronized(session) {
            try {
                val profileData = profileRepository.getProfileDataByUserId(getProfileData.accountId)
                if(profileData==null) {
                    session.sendResponse(MobileResponse(requestType, requestId, SuccessResponse()))
                }else{
                    session.sendResponse(MobileResponse(requestType, requestId, profileData))
                }
            } catch (e: Exception) {
                session.sendResponse(MobileResponse(requestType, requestId, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }

        }
    }
}

