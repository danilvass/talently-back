package com.tripled.talentlyapp.core.requestProcessor

import GetPortfoliosByAccountIdOperation
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.portfolioRepository
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.postRepository
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.profileRepository
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.subscribeRepository
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.userCredentialsRepository
import com.tripled.talentlyapp.core.async.AsyncExecutor
import com.tripled.talentlyapp.core.extensions.sendResponse
import com.tripled.talentlyapp.core.network.ws.models.requests.MobileRequest
import com.tripled.talentlyapp.core.network.ws.models.requests.data.*
import com.tripled.talentlyapp.core.network.ws.models.response.MobileResponse
import com.tripled.talentlyapp.core.network.ws.models.response.data.Comment
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.core.operations.*
import com.tripled.talentlyapp.core.requests.MobileRequestType
import com.tripled.talentlyapp.utils.loggerFor
import org.slf4j.Logger
import org.springframework.web.socket.WebSocketSession


class MobileRequestProcessorDelegateImpl : MobileRequestProcessorDelegate {

    private companion object {
        val logger: Logger = loggerFor(MobileRequestProcessorDelegateImpl::class.java)
    }

    private val executor: AsyncExecutor = AsyncExecutor(50, 10, 100, 10)

    override fun onRequest(req: MobileRequest, session: WebSocketSession) {
        if (req.type.checkIsAuthRequired()) {
            executeOperationWithTokenAndUserId(req, session)
        } else {
            executeOperationWithoutTokenAndUserId(req, session)
        }
    }

    private fun executeOperationWithoutTokenAndUserId(req: MobileRequest, session: WebSocketSession) {

        when (req.type) {
            MobileRequestType.SIGN_IN -> {
                val data = req.data as SignInRequestData
                executor.execute(SignInOperation(req.type, req.id, data, session, userCredentialsRepository))
            }
            MobileRequestType.SIGN_UP -> {
                val data = req.data as SignUpData
                executor.execute(SignUpOperation(req.type, req.id, data, session, userCredentialsRepository))
            }
            MobileRequestType.CONNECT -> {
                executor.execute(ConnectOperation(req, session, userCredentialsRepository))
            }
        }
    }

    private fun executeOperationWithTokenAndUserId(req: MobileRequest, session: WebSocketSession) {

        if (!checkUserToken(req, session)) {
            return
        }

        when (req.type) {
            MobileRequestType.SIGN_OUT -> {
                executor.execute(SignOutOperation(req, session, userCredentialsRepository))
            }
            MobileRequestType.GET_PROFILE_DATA -> {
                val data = req.data as GetProfileRequestData
                executor.execute(GetProfileDataOperation(req.id,req.type,data,session, profileRepository))
            }
            MobileRequestType.SET_PROFILE_DATA -> {
                val data = req.data as SetProfileRequestData
                executor.execute(SetProfileDataOperation(req.type, req.id, req.userId, data, session, profileRepository))
            }
            MobileRequestType.GET_PORTFOLIO_DATA -> {
                val data = req.data as GetPortfolioRequestData
                executor.execute(GetPortfolioContentsOperation(req.type, req.id, data, session, portfolioRepository))
            }
            MobileRequestType.GET_OWN_PORTFOLIOS -> {
                executor.execute(GetOwnPortfoliosOperation(req, session, portfolioRepository))
            }
            MobileRequestType.GET_FEED_POSTS -> {
                val postId = req.data as String
                executor.execute(GetFeedPosts(req, req.userId, session, postRepository, postId))
            }
            MobileRequestType.SUBSCRIBE -> {
                val data = req.data as SubscribeRequestData
                executor.execute(SubscribeOperation(req.type, req.id, req.userId, data, session, subscribeRepository))
            }
            MobileRequestType.GET_ALL_SUBSCRIPTIONS -> {
                executor.execute(GetAllSubscriptionsOperation(req, session, subscribeRepository))
            }
            MobileRequestType.POPULAR_BY_CATEGORY -> {
                val category = req.data as String
                executor.execute(PopularByCategoryOperation(req, session, portfolioRepository, category))
            }
            MobileRequestType.DELETE_PORTFOLIO -> {
                val data = req.data as DeletePortfolioRequestData
                executor.execute(DeletePortfolioOperation(req.type, req.id, data, session, portfolioRepository))
            }
            MobileRequestType.DELETE_POST -> {
                val data = req.data as DeletePostRequestData
                executor.execute(DeletePostOperation(req.type, req.id, data, session, postRepository))
            }
            MobileRequestType.GET_ACCOUNT_PORTFOLIOS -> {
                val accountId = req.data as String
                executor.execute(GetPortfoliosByAccountIdOperation(req, session, portfolioRepository, accountId))
            }
            MobileRequestType.ADD_COMMENT -> {
                var comment = req.data as Comment
                comment.accountId = req.userId
                executor.execute(AddCommentOperation(req.type, req.id, comment, session, postRepository))
            }
            MobileRequestType.UNSUBSCRIBE -> {
                val data = req.data as UnsubscribeRequestData
                executor.execute(UnsubscribeOperation(req.type, req.id, req.userId, data, session, subscribeRepository))
            }
            MobileRequestType.GET_SUBSCRIBES -> {
                executor.execute(GetSubscribesIds(req, session, subscribeRepository))
            }
            MobileRequestType.SEARCH_PORTFOLIO -> {
                val search = req.data as String
                executor.execute(SearchPortfolioOperation(req, session,portfolioRepository, search))
            }
            MobileRequestType.LIKE -> {
                val data = req.data as LikeRequestData
                executor.execute(AddLikeOperation(req.type, req.id,req.userId,data, session, postRepository))
            }

        }
    }

    private fun checkUserToken(req: MobileRequest, session: WebSocketSession): Boolean {
        if (userCredentialsRepository.checkIfUserTokenIsValid(req.userId, req.token).isNullOrEmpty()) {
            session.sendResponse(MobileResponse(req.type, req.id, null,
                    ErrorResponseData(ErrorType.BUSINESS_ERROR, "User Token", "User Token not valid")))
            return false
        }
        return true
    }

}