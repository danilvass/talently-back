package com.tripled.talentlyapp.database.repositories.UserCredentialsRepository

import com.tripled.talentlyapp.core.network.ws.models.response.data.SignInResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.SignUpResponseData


interface UserCredentialsRepository {

    fun checkUserCredentials(login: String, password: String): SignInResponseData?
    fun signUp(login: String, password: String): SignUpResponseData?
    fun checkIfUserTokenIsValid(userId: String, authToken: String): String?
    fun signOut(authToken: String, userId: String)
}