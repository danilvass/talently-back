package com.tripled.talentlyapp.database.Mappers

import com.tripled.talentlyapp.core.network.ws.models.response.data.PortfolioSubscription
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select

interface SubscribeMapper {


    @Insert("Insert into portfolio_subscriptions values(#{id},#{portfolioId},#{accountId}) ")
    fun addPortfolioSubscription(id: String, portfolioId: String, accountId: String)

    @Select("SELECT ap.id,ap.name,a.login,ap.portfolio_image " +
            "from portfolio_subscriptions ps " +
            "left join account_portfolio ap on ap.id = ps.portfolio_id " +
            "left join account a on a.account_id=#{accountId} " +
            "where ps.account_id=#{accountId}  ")
    fun getAllSubscriptions(accountId: String): ArrayList<PortfolioSubscription>?

    @Select("SELECT portfolio_id FROM portfolio_subscriptions " +
            "where account_id=#{accountId}")
    fun getSubscribesIds(accountId: String): ArrayList<String>?

    @Delete("Delete from portfolio_subscriptions " +
            "where account_id=#{accountId} and portfolio_id=#{portfolioId}")
    fun unsubscribe(accountId: String, portfolioId: String)

}