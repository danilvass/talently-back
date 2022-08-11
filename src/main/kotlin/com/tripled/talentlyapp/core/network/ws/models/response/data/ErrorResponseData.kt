package com.tripled.talentlyapp.core.network.ws.models.response.data

import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType

data class ErrorResponseData (val type: ErrorType, val code:String, val message:String)