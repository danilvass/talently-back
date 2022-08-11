package com.tripled.talentlyapp.database.repositories.MediaRepository

import com.tripled.talentlyapp.TalentlyAppApplication.Companion.dbConnection
import com.tripled.talentlyapp.database.Mappers.MediaMapper
import com.tripled.talentlyapp.utils.sessionCloseAndCommit
import org.apache.ibatis.session.SqlSession

class MediaRepository : IMediaRepository {

    override fun addAvatarImagePath(userId: String, fileName: String) {
        val session = dbConnection.openSession()
        val authMapper = session.getMapper(MediaMapper::class.java)
        authMapper.addUserAvatarImagePath(userId, fileName)
        sessionCloseAndCommit(session)
    }

    override fun getAvatarImageNameByUserId(userId: String): String {
        val session = dbConnection.openSession()
        val authMapper = session.getMapper(MediaMapper::class.java)
        val fileName=authMapper.getAvatarImageNameByUserId(userId)
        sessionCloseAndCommit(session)
        return fileName
    }


}