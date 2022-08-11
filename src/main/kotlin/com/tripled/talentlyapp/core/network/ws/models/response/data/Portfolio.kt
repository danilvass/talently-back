package com.tripled.talentlyapp.core.network.ws.models.response.data

import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.PortfolioCategory

data class Portfolio(val portfolioId: String, val name: String, val description: String?,
                                  val imageUrl: String?, val subscriptionCount: String, val likesCount: String, val accountName: String, val category: PortfolioCategory, val accountId: String) {
}

data class Portfolios(val portfolios: ArrayList<Portfolio>?)