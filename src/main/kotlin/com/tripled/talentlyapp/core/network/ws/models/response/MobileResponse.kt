package com.tripled.talentlyapp.core.network.ws.models.response

import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.requests.MobileRequestType


data class MobileResponse(val action: MobileRequestType, val requestId: String, val data: Any? , val error: ErrorResponseData? = null)