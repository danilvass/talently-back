package com.tripled.talentlyapp.core.operations

import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.Posts
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.database.repositories.PostRepository.IPostRepository
import org.springframework.web.socket.WebSocketSession
import java.lang.Exception

class GetFeedPosts(private val request: MobileRequest, private val userId: String,
                   private val session: WebSocketSession,
                   private val postRepository: IPostRepository, private val postId: String?) : Runnable {
    override fun run() {
        synchronized(session) {
            try {
                val newsFeed = postRepository.getNewsFeedUntilPostOrReturnAllFeed(postId, userId)
               session.sendResponse(MobileResponse(request.type, request.id, Posts(newsFeed)))

            } catch (e: Exception) {
                session.sendResponse(MobileResponse(request.type, request.id, null,
                        ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString())))
            }
        }
    }
}