package com.tripled.talentlyapp.database.repositories.PortfolioRepository

import com.tripled.talentlyapp.core.network.ws.models.response.data.*


interface IPortfolioRepository {
    fun getPortfolioData(portfolioId:String):ArrayList<Post>
    fun createPortfolio(userId:String, filename: String, description: String?, name: String, category: String)
    fun deletePortfolio(portfolioId: String)
    fun getPortfolios(userId:String):ArrayList<Portfolio>
    fun getPopularByCategory(category: String): ArrayList<Portfolio>?
    fun searchPortfilio(search: String?): ArrayList<Portfolio>?
}