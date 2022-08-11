package com.tripled.talentlyapp.database.repositories.ProfileRepository

import com.tripled.talentlyapp.core.network.ws.models.response.data.GetProfileResponseData

interface IProfileRepository {

    fun getProfileDataByUserId(userId: String): GetProfileResponseData

    fun setProfileDataByUserId(userId: String, gender: String?, age: Int?, description: String?)


}