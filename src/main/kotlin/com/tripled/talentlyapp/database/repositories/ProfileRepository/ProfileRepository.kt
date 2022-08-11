package com.tripled.talentlyapp.database.repositories.ProfileRepository


import com.tripled.talentlyapp.TalentlyAppApplication.Companion.dbConnection
import com.tripled.talentlyapp.core.network.ws.models.response.data.GetProfileResponseData
import com.tripled.talentlyapp.database.Mappers.ProfileDataMapper
import com.tripled.talentlyapp.utils.sessionCloseAndCommit


class ProfileRepository:IProfileRepository {

    override fun getProfileDataByUserId(userId: String): GetProfileResponseData {
        val session = dbConnection.openSession()
        val profileMapper = session.getMapper(ProfileDataMapper::class.java)
        val profileData = profileMapper.getProfileDataByUserId(userId)
        sessionCloseAndCommit(session)
        return profileData
    }

    override fun setProfileDataByUserId(userId: String, gender: String?, age: Int?, description: String?) {
        val session = dbConnection.openSession()
        val profileMapper = session.getMapper(ProfileDataMapper::class.java)
        profileMapper.setProfileDataByUserId(userId,gender,age,description)
        sessionCloseAndCommit(session)
    }



}