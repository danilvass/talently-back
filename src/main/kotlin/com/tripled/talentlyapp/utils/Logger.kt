package com.tripled.talentlyapp.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T> loggerFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)
