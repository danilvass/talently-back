package com.tripled.talentlyapp.core.network.ws.models.response.data

data class PostForFeedSql (val id: String, val type: String, val description: String?,
                     var postData: String, val accountId: String, val portfolioId: String, val userAvatarUrl: String?, val accountName: String )