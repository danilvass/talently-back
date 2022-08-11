package com.tripled.talentlyapp.core.network.ws

import com.tripled.talentlyapp.core.requestProcessor.MobileRequestProcessorDelegateImpl
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration @EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer {

    private val mobileRequestProcessorDelegate = MobileRequestProcessorDelegateImpl()

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        val handler = WebSocketHandler(mobileRequestProcessorDelegate)
        registry.addHandler(handler, "/ws").setAllowedOrigins("*")

    }

}
