package com.tripled.talentlyapp.database.configuration


import com.tripled.talentlyapp.database.Interfaces.DatabaseInterface
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder



object DatabaseConfiguration : DatabaseInterface {


    override fun getConnection():SqlSessionFactory {
        val resource = "DatabaseConfigFile.xml"
        val inputStream = Resources.getResourceAsStream(resource)
        return SqlSessionFactoryBuilder().build(inputStream)
    }



}