package com.tripled.talentlyapp.core.network.ws.models.response.data

data class CommentData (val comment:String, val author:String,  val date:String ) {
    constructor():this("","","")
}