package com.tripled.talentlyapp.database.Mappers

import com.tripled.talentlyapp.core.network.ws.models.response.data.SignUpResponseData
import org.apache.ibatis.annotations.*
import org.apache.ibatis.mapping.StatementType
import com.tripled.talentlyapp.core.network.ws.models.response.data.SignInResponseData

interface AuthorizationMapper {


    @Select("call checkUserCredentials(#{login},#{password})")
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(SignInResponseData::class)
    fun checkUserCredentials(login: String, password: String): SignInResponseData?

    @Select("select account_id from account_tokens where account_id = #{userId} and token = #{authToken}")
    fun checkIfUserTokenIsValid(userId: String, authToken: String): String?


    @Select("CALL SignUp( #{login} , #{password})")
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(SignUpResponseData::class)
    fun singUp(login: String, password: String): SignUpResponseData?

    @Delete("DELETE FROM account_tokens WHERE token= #{authToken} and account_id = #{userId}")
    fun signOut(authToken: String, userId: String)

}
