package com.tripled.talentlyapp.utils

inline fun <reified T : Enum<T>> createEnumFrom(type: String): T? {
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: IllegalArgumentException) {
        null
    }
}