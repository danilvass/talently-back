package com.tripled.talentlyapp.core.network.ws.models.response.data

data class Comment(val postId: String, val accountName: String?, var accountId: String?, val text: String) {
}

data class Comments(val commnets: ArrayList<Comment>?)