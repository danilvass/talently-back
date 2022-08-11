package com.tripled.talentlyapp.database.repositories.MediaRepository

interface IMediaRepository {

    fun addAvatarImagePath(userId: String, fileName: String)
    fun getAvatarImageNameByUserId(userId:String):String

}