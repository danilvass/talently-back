package com.tripled.talentlyapp.core.requests

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty

enum class MobileRequestType {
    @JsonProperty("SIGN_IN")
    SIGN_IN,

    @JsonProperty("SIGN_UP")
    SIGN_UP,

    @JsonProperty("CONNECT")
    CONNECT,

    @JsonProperty("SIGN_OUT")
    SIGN_OUT,

    @JsonProperty("GET_PROFILE_DATA")
    GET_PROFILE_DATA,

    @JsonProperty("SET_PROFILE_DATA")
    SET_PROFILE_DATA,

    @JsonProperty("GET_PORTFOLIO_DATA")
    GET_PORTFOLIO_DATA,

    @JsonProperty("GET_OWN_PORTFOLIOS")
    GET_OWN_PORTFOLIOS,

    @JsonProperty("GET_ALL_SUBSCRIPTIONS")
    GET_ALL_SUBSCRIPTIONS,

    @JsonProperty("GET_FEED_POSTS")
    GET_FEED_POSTS,

    @JsonProperty("SUBSCRIBE")
    SUBSCRIBE,

    @JsonProperty("DELETE_PORTFOLIO")
    DELETE_PORTFOLIO,

    @JsonProperty("DELETE_POST")
    DELETE_POST,

    @JsonProperty("GET_ACCOUNT_PORTFOLIOS")
    GET_ACCOUNT_PORTFOLIOS,

    @JsonProperty("ADD_COMMENT")
    ADD_COMMENT,

    @JsonProperty("POPULAR_BY_CATEGORY")
    POPULAR_BY_CATEGORY,

    @JsonProperty("UNSUBSCRIBE")
    UNSUBSCRIBE,

    @JsonProperty("SEARCH_PORTFOLIO")
    SEARCH_PORTFOLIO,

    @JsonProperty("GET_SUBSCRIBES")
    GET_SUBSCRIBES,

    @JsonProperty("LIKE")
    LIKE;


    fun checkIsAuthRequired(): Boolean {
        return when (this) {
            SIGN_IN -> false
            SIGN_UP -> false
            CONNECT -> false
            SIGN_OUT -> true
            GET_PROFILE_DATA -> true
            SET_PROFILE_DATA -> true
            GET_PORTFOLIO_DATA -> true
            GET_OWN_PORTFOLIOS -> true
            SUBSCRIBE -> true
            GET_ALL_SUBSCRIPTIONS -> true
            POPULAR_BY_CATEGORY -> true
            DELETE_PORTFOLIO -> true
            DELETE_POST -> true
            GET_ACCOUNT_PORTFOLIOS -> true
            GET_FEED_POSTS -> true
            UNSUBSCRIBE -> true
            GET_SUBSCRIBES -> true
            ADD_COMMENT -> true
            SEARCH_PORTFOLIO -> true
            LIKE -> true
        }
    }

}