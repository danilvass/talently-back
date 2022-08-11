package com.tripled.talentlyapp.database.repositories.SubscribeRepository

import com.tripled.talentlyapp.core.network.ws.models.response.data.PortfolioSubscriptions

interface ISubscribeRepository {
    fun addPortfolioSubscription(accountId: String, portfolioId: String)
    fun getAllSubscriptions(accountId: String): PortfolioSubscriptions?
    fun getSubscribesIds(accountId: String): ArrayList<String>?
    fun unsubscribe(accountId: String, portfolioId: String)
}