package com.tripled.talentlyapp.database.repositories.UserCredentialsRepository

import com.tripled.talentlyapp.TalentlyAppApplication.Companion.dbConnection
import com.tripled.talentlyapp.core.network.ws.models.response.data.SignInResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SignUpResponseData
import com.tripled.talentlyapp.database.Mappers.AuthorizationMapper
import com.tripled.talentlyapp.utils.sessionCloseAndCommit


class DbUserCredentialsRepository: UserCredentialsRepository {


    override fun checkUserCredentials(login: String, password: String): SignInResponseData? {
        val session = dbConnection.openSession()
        val authMapper = session.getMapper(AuthorizationMapper::class.java)
        val checkUserCredentialsResult = authMapper.checkUserCredentials(login, password)
        sessionCloseAndCommit(session)
        return checkUserCredentialsResult
    }

    override fun signUp(login: String, password: String): SignUpResponseData? {
        val session = dbConnection.openSession()
        val authMapper = session.getMapper(AuthorizationMapper::class.java)
        val data = authMapper.singUp(login, password)
        sessionCloseAndCommit(session)
        return data
    }

    override fun checkIfUserTokenIsValid(userId: String, authToken: String): String? {
        val session = dbConnection.openSession()
        val authMapper = session.getMapper(AuthorizationMapper::class.java)
        val result = authMapper.checkIfUserTokenIsValid(userId, authToken)
        sessionCloseAndCommit(session)
        return result
    }

    override fun signOut(authToken: String, userId: String) {
        val session = dbConnection.openSession()
        val authMapper = session.getMapper(AuthorizationMapper::class.java)
        authMapper.signOut(authToken, userId)
        sessionCloseAndCommit(session)
    }





}