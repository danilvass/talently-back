package com.tripled.talentlyapp.core.requestProcessor

import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import org.springframework.web.socket.WebSocketSession

interface MobileRequestProcessorDelegate {
    fun onRequest(req: MobileRequest, session: WebSocketSession)
}