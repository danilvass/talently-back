package com.tripled.talentlyapp.database.repositories.PortfolioRepository

import com.tripled.talentlyapp.TalentlyAppApplication.Companion.dbConnection
import com.tripled.talentlyapp.core.network.ws.models.response.data.*
import com.tripled.talentlyapp.database.Mappers.PortfolioMapper
import com.tripled.talentlyapp.database.Mappers.PostMapper
import com.tripled.talentlyapp.database.repositories.PostRepository.PostRepository
import com.tripled.talentlyapp.utils.sessionCloseAndCommit

class PortfolioRepository : IPortfolioRepository {

    override fun getPortfolioData(portfolioId: String): ArrayList<Post> {

        val session = dbConnection.openSession()
        val portfolioMapper = session.getMapper(PortfolioMapper::class.java)
        val basePortfolioData =portfolioMapper.getPortfolioBaseData(portfolioId)
        return if(basePortfolioData==null){
            sessionCloseAndCommit(session)
            arrayListOf();
        }else{
            var portfolioContent = fillPortfolioDataFromBaseData(basePortfolioData, portfolioId)
            val postMapper = session.getMapper(PostMapper::class.java)
            var filledPortfolioContent = getCommentsIntoPosts(fillPortfolioContentData(portfolioMapper, portfolioContent), postMapper)
            sessionCloseAndCommit(session)
            filledPortfolioContent
        }

    }
    //TODO: rewrite this shit. I think getPortfolioData must be in PostRepository. Change my mind.
    private fun getCommentsIntoPosts(posts: ArrayList<Post>, mapper: PostMapper): ArrayList<Post> {
        posts.forEach {
            it.comments = mapper.getAllCommentsByPostId(it.id)
        }
        return posts
    }
    private fun fillPortfolioDataFromBaseData(portfolioBaseData: ArrayList<BasePortfolioData>?, portfolioId: String): ArrayList<Post> {
        var portfolioContent = arrayListOf<Post>()
        if (portfolioBaseData != null && portfolioBaseData.count() > 0) {
                portfolioBaseData.forEach {
                    portfolioContent.add(Post(it.id, it.type, it.description, null, arrayListOf(), it.accountId, portfolioId, null, null))
            }
        }
        return portfolioContent;
    }

    private fun fillPortfolioContentData(portfolioMapper: PortfolioMapper, filledPortfolioContent: ArrayList<Post>): ArrayList<Post> {
        filledPortfolioContent.forEach {
            val contentPaths = portfolioMapper.getPortfolioPostData(it.id, it.type)
            if (contentPaths.count() >= 1) {
                it.postData = contentPaths
            }
        }
        return filledPortfolioContent
    }

    override fun createPortfolio(userId: String, filename: String, description: String?, name: String, category: String) {
        val session = dbConnection.openSession()
        val portfolioMapper = session.getMapper(PortfolioMapper::class.java)
        portfolioMapper.createPortfolio(userId, name, filename, description, category)
        sessionCloseAndCommit(session)
    }

    override fun getPortfolios(userId: String): ArrayList<Portfolio> {
        val session = dbConnection.openSession()
        val portfolioMapper = session.getMapper(PortfolioMapper::class.java)
        val portfolios = portfolioMapper.getPortfolios(userId)
        sessionCloseAndCommit(session)
        return portfolios
    }

    override fun getPopularByCategory(category: String): ArrayList<Portfolio>? {
        val session = dbConnection.openSession()
        val portfolioMapper = session.getMapper(PortfolioMapper::class.java)
        val portfolios = portfolioMapper.getPortfoliosByCategory(category)
        if (portfolios.count() > 0) {
            portfolios.sortWith(compareBy({ it.subscriptionCount }))
        }
        sessionCloseAndCommit(session)
        return portfolios
    }

    override fun searchPortfilio(search: String?): ArrayList<Portfolio>? {
        val session = dbConnection.openSession()
        val portfolioMapper = session.getMapper(PortfolioMapper::class.java)
        val posts = portfolioMapper.searchPortfolio(search)
        sessionCloseAndCommit(session)
        return posts
    }

    override fun deletePortfolio(portfolioId: String) {
        val session = dbConnection.openSession()
        val portfolioMapper = session.getMapper(PortfolioMapper::class.java)
        portfolioMapper.deletePortfolio(portfolioId)
        sessionCloseAndCommit(session)
    }


}