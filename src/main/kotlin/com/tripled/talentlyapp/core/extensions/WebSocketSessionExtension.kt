package com.tripled.talentlyapp.core.extensions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

  fun WebSocketSession.sendResponse(response: MobileResponse) {
    val mapper = jacksonObjectMapper()
    this.sendMessage(TextMessage(mapper.writeValueAsString(response)))
}