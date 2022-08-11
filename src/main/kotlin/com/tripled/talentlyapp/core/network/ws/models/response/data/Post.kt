package com.tripled.talentlyapp.core.network.ws.models.response.data

data class Post(val id: String, val type: String, val description: String?,
                var postData: ArrayList<String>?, var comments: ArrayList<Comment>,
                val accountId: String?, val portfolioId: String?, val userAvatarUrl: String?, val accountName: String?)