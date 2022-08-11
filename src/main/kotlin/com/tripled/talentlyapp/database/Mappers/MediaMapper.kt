package com.tripled.talentlyapp.database.Mappers

import org.apache.ibatis.annotations.*

interface MediaMapper {

    @Update("update account_profile set avatar_url = #{fileName} where account_id = #{userId}")
    fun addUserAvatarImagePath(userId:String,fileName:String)

    @Select("select avatar_url from account_profile where account_id=#{userId}")
    fun getAvatarImageNameByUserId(userId:String):String
}