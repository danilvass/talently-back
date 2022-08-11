package com.tripled.talentlyapp.core.network.ws

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.*
import com.tripled.talentlyapp.core.network.ws.errors.IncorrectMobileRequestDataException
import com.tripled.talentlyapp.core.network.ws.errors.IncorrectMobileRequestException
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.requests.data.*
import com.tripled.talentlyapp.core.network.ws.models.response.data.Comment
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.core.requests.MobileRequestType.*
import com.tripled.talentlyapp.core.requestProcessor.MobileRequestProcessorDelegate
import com.tripled.talentlyapp.utils.loggerFor
import org.slf4j.Logger
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class WebSocketHandler(private val delegate: MobileRequestProcessorDelegate) : TextWebSocketHandler() {

    private companion object {
        val logger: Logger = loggerFor(WebSocketHandler::class.java)
    }

    //MARK: - private properties
    private var sessionList: ConcurrentHashMap<WebSocketSession, String> = ConcurrentHashMap()
    private val mapper = jacksonObjectMapper()

    //MARK: - connection events
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val sessionId: String = UUID.randomUUID().toString()
        sessionList[session] = sessionId
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionList.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = message.payload

        logger.debug("Received client message: $json")

        try {
            val req = parseMobileRequest(json)
            delegate.onRequest(req, session)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Throws(IncorrectMobileRequestException::class,
            JsonParseException::class,
            JsonProcessingException::class)
    private fun parseMobileRequest(jsonString: String): MobileRequest {
        val json: JsonNode = mapper.readTree(jsonString)
        val id: String = json.get("requestId")?.asText() ?: throw IncorrectMobileRequestException(jsonString)
        val userId: String? = json.get("userId")?.asText()
        val token: String? = json.get("authToken")?.asText()

        val requestType = mapper.treeToValue(json.get("action"), MobileRequestType::class.java)
                ?: throw IncorrectMobileRequestException(jsonString)

        val jsonData: JsonNode? = json.get("data")
        val requestData = parseMobileRequestData(requestType, jsonData)

        return MobileRequest.create(id, requestType, userId, token, requestData)
                ?: throw IncorrectMobileRequestException(jsonString)
    }

    private fun parseMobileRequestData(requestType: MobileRequestType, data: JsonNode?): Any? {

        val requiredData: Any? = when (requestType) {
            SIGN_IN -> mapper.treeToValue(data, SignInRequestData::class.java)
            SIGN_UP -> mapper.treeToValue(data, SignUpData::class.java)
            CONNECT -> return null
            SIGN_OUT -> return null
            GET_PROFILE_DATA -> mapper.treeToValue(data, GetProfileRequestData::class.java)
            SET_PROFILE_DATA -> mapper.treeToValue(data, SetProfileRequestData::class.java)
            GET_PORTFOLIO_DATA -> mapper.treeToValue(data, GetPortfolioRequestData::class.java)
            GET_OWN_PORTFOLIOS -> return null
            SUBSCRIBE -> mapper.treeToValue(data, SubscribeRequestData::class.java)
            GET_ALL_SUBSCRIPTIONS -> return null
            POPULAR_BY_CATEGORY -> data?.get("category")?.asText()
            DELETE_PORTFOLIO -> mapper.treeToValue(data, DeletePortfolioRequestData::class.java)
            DELETE_POST -> mapper.treeToValue(data, DeletePostRequestData::class.java)
            GET_FEED_POSTS -> data?.get("lastPostId")?.asText()
            GET_ACCOUNT_PORTFOLIOS -> data?.get("accountId")?.asText()
            UNSUBSCRIBE -> mapper.treeToValue(data, UnsubscribeRequestData::class.java)
            GET_SUBSCRIBES -> return null
            ADD_COMMENT -> mapper.treeToValue(data, Comment::class.java)
            SEARCH_PORTFOLIO -> data?.get("search")?.asText()
            LIKE -> mapper.treeToValue(data, LikeRequestData::class.java)
        }

        return requiredData
                ?: throw IncorrectMobileRequestDataException("Request type: ${requestType.name}, data: " + (data?.asText()
                        ?: "Empty data"))
    }

}
