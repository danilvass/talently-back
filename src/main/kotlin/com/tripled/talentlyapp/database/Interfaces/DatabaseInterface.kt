package com.tripled.talentlyapp.database.Interfaces

import org.apache.ibatis.session.SqlSessionFactory

interface DatabaseInterface {
    fun  getConnection():SqlSessionFactory
}