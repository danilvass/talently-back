package com.tripled.talentlyapp.database.Mappers

import com.tripled.talentlyapp.core.network.ws.models.response.data.GetProfileResponseData
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface ProfileDataMapper {

    @Select("SELECT ag.gender,ap.age,ap.description,ap.avatar_url, " +
            "(Select Count(ps.id) from account_portfolio ap " +
            "left join portfolio_subscriptions ps on ps.portfolio_id = ap.id " +
            "where ap.account_id=#{userId}) "+
            "FROM account_profile ap "+
            " left join account_gender ag "+
            "on ag.id=ap.gender "+
            "WHERE  ap.account_id=#{userId} ")
    fun getProfileDataByUserId(userId:String):GetProfileResponseData

    @Update("Call UpdateProfileData(#{userId},#{gender},#{age},#{description})")
    fun setProfileDataByUserId(userId:String,gender:String?,age:Int?,description:String?)


}