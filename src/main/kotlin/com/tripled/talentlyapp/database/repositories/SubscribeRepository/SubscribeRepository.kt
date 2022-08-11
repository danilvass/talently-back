package com.tripled.talentlyapp.database.repositories.SubscribeRepository

import com.tripled.talentlyapp.TalentlyAppApplication.Companion.dbConnection
import com.tripled.talentlyapp.core.network.ws.models.response.data.PortfolioSubscriptions
import com.tripled.talentlyapp.database.Mappers.SubscribeMapper
import com.tripled.talentlyapp.utils.sessionCloseAndCommit
import java.util.*
import kotlin.collections.ArrayList

class SubscribeRepository : ISubscribeRepository {

    override fun addPortfolioSubscription(accountId: String, portfolioId: String) {
        val session = dbConnection.openSession()
        val subscribeMapper = session.getMapper(SubscribeMapper::class.java)
        subscribeMapper.addPortfolioSubscription(UUID.randomUUID().toString(), portfolioId, accountId)
        sessionCloseAndCommit(session)
    }

    override fun getAllSubscriptions(accountId: String): PortfolioSubscriptions? {
        val session = dbConnection.openSession()
        val subscribeMapper = session.getMapper(SubscribeMapper::class.java)
        val allSubscriptions = subscribeMapper.getAllSubscriptions(accountId)
        return if (allSubscriptions == null) {
            sessionCloseAndCommit(session)
            null
        } else {
            sessionCloseAndCommit(session)
            PortfolioSubscriptions(allSubscriptions.count(), allSubscriptions)
        }
    }

    override fun getSubscribesIds(accountId: String): ArrayList<String>? {
        val session = dbConnection.openSession()
        val subscribeMapper = session.getMapper(SubscribeMapper::class.java)
        val subscribesIds = subscribeMapper.getSubscribesIds(accountId)
        sessionCloseAndCommit(session)
        return subscribesIds
    }

    override fun unsubscribe(accountId: String, portfolioId: String) {
        val session = dbConnection.openSession()
        val subscribeMapper = session.getMapper(SubscribeMapper::class.java)
        subscribeMapper.unsubscribe(accountId,portfolioId)
        sessionCloseAndCommit(session)
    }
}

