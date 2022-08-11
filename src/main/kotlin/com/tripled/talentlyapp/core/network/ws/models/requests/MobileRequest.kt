package com.tripled.talentlyapp.core.network.ws.models.requests

import com.tripled.talentlyapp.core.requests.MobileRequestType

data class MobileRequest(
        val id: String,
        val type: MobileRequestType,
        val userId: String,
        val token: String,
        var data: Any? = null
) {
    companion object {
        fun create(id: String,
                   type: MobileRequestType,
                   userId: String?,
                   token: String?,
                   data: Any? = null): MobileRequest? {
            if (type.checkIsAuthRequired() && (userId == null || token == null)) return null
            val userIdUnwrapped = userId ?: ""
            val tokenUnwrapped = token ?: ""
            return MobileRequest(id, type, userIdUnwrapped, tokenUnwrapped, data)
        }
    }
}