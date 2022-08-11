package com.tripled.talentlyapp.database.Mappers

import org.apache.ibatis.mapping.StatementType

import com.tripled.talentlyapp.core.network.ws.models.response.data.BasePortfolioData
import org.apache.ibatis.annotations.*
import com.tripled.talentlyapp.core.network.ws.models.response.data.Portfolio
import org.apache.ibatis.annotations.ResultType


interface PortfolioMapper {

    @Insert("Call CreatePortfolio(#{userId},#{name},#{filename},#{description},#{category})")
    fun createPortfolio(userId: String, name: String, filename: String, description: String?, category: String)


    @Select("" +
            " SELECT p.id, ct.type, p.description, ap.account_id as accountId " +
            " FROM post p " +
            "   left join content_type ct " +
            "   on ct.id=p.content_type " +
            "       Left join account_portfolio ap" +
            "       on ap.id = p.portfolio_id" +
            " where p.portfolio_id=#{portfolioId} " +
            " order by t_create Desc")
    fun getPortfolioBaseData(portfolioId: String): ArrayList<BasePortfolioData>?


    @Select("call getPostData(#{postId},#{contentType})")
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(ArrayList::class)
    fun getPortfolioPostData(postId: String, contentType: String): ArrayList<String>

    @Select("SELECT " +
            " ap.id as portfolioId," +
            " ap.name," +
            " ap.description," +
            " ap.portfolio_image as imageUrl," +
            " (Select count(*) From portfolio_subscriptions ps" +
            "  Where ps.portfolio_id = ap.id) as subscriptionCount," +
            " (Select count(*) From post_likes pl" +
            "  Where pl.portfolio_id = ap.id) as likesCount," +
            "  acc.login as accountName," +
            "  pc.category," +
            "  #{userId}" +
            " FROM account_portfolio ap " +
            "   left join portfolio_category pc" +
            "   on pc.id=ap.category " +
            "       Left join account acc" +
            "       on ap.account_id = acc.account_id" +
            " where ap.account_id=#{userId}")
    fun getPortfolios(userId: String): ArrayList<Portfolio>


    @Select("Select" +
            "    ap.id as portfolioId," +
            "    ap.name as portfolioName," +
            "    ap.description," +
            "    ap.portfolio_image as imageUrl," +
            "    (Select count(*) From portfolio_subscriptions ps" +
            "     Where ps.portfolio_id = ap.id) as subscriptionCount," +
            "    (Select count(*) From post_likes pl" +
            "     Where pl.portfolio_id = ap.id) as likesCount," +
            "    acc.login as accountName," +
            "     #{category} as category," +
            "     acc.account_id" +
            "    From account_portfolio ap" +
            "       Left join account acc" +
            "       on ap.account_id = acc.account_id" +
            "           Left join portfolio_category pc" +
            "           on ap.category = pc.id" +
            "    where pc.category = #{category}")
    fun getPortfoliosByCategory(category: String): ArrayList<Portfolio>

    @Delete("delete from account_portfolio " +
            "where id = #{portfolioId}")
    fun deletePortfolio(portfolioId: String)

    @Select("Select" +
            "    ap.id as portfolioId," +
            "    ap.name as portfolioName," +
            "    ap.description," +
            "    ap.portfolio_image as imageUrl," +
            "    (Select count(*) From portfolio_subscriptions ps" +
            "     Where ps.portfolio_id = ap.id) as subscriptionCount," +
            "    (Select count(*) From post_likes pl" +
            "     Where pl.portfolio_id = ap.id) as likesCount," +
            "    acc.login as accountName," +
            "     pc.category as category," +
            "     acc.account_id" +
            "    From account_portfolio ap" +
            "       Left join account acc" +
            "       on ap.account_id = acc.account_id" +
            "           Left join portfolio_category pc" +
            "           on ap.category = pc.id" +
            "    where pc.category like CONCAT('%',#{search},'%')" +
            "    or acc.login like CONCAT('%',#{search},'%')" +
            "    or ap.name like CONCAT('%',#{search},'%')")
    fun searchPortfolio(search: String?): ArrayList<Portfolio>
}