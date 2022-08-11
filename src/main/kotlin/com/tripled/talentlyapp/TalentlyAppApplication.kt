package com.tripled.talentlyapp

import com.tripled.talentlyapp.database.Interfaces.DatabaseInterface
import com.tripled.talentlyapp.database.repositories.MediaRepository.IMediaRepository
import com.tripled.talentlyapp.database.repositories.PortfolioRepository.IPortfolioRepository
import com.tripled.talentlyapp.database.repositories.PostRepository.IPostRepository
import com.tripled.talentlyapp.database.repositories.ProfileRepository.IProfileRepository
import com.tripled.talentlyapp.database.repositories.SubscribeRepository.ISubscribeRepository
import com.tripled.talentlyapp.database.repositories.UserCredentialsRepository.UserCredentialsRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.ClassPathXmlApplicationContext


@SpringBootApplication
class TalentlyAppApplication {
    companion object {
        val dbConnection = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("DatabaseConfiguration", DatabaseInterface::class.java).getConnection()
        val userCredentialsRepository: UserCredentialsRepository = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("DbUserCredentialsRepository", UserCredentialsRepository::class.java)
        val mediaRepository: IMediaRepository = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("MediaRepository", IMediaRepository::class.java)
        val portfolioRepository: IPortfolioRepository = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("PortfolioRepository", IPortfolioRepository::class.java)
        val profileRepository: IProfileRepository = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("ProfileRepository", IProfileRepository::class.java)
        val postRepository: IPostRepository = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("PostRepository", IPostRepository::class.java)
        val subscribeRepository: ISubscribeRepository = ClassPathXmlApplicationContext("DIConfig.xml")
                .getBean("SubscribeRepository", ISubscribeRepository::class.java)

    }
}

fun main(args: Array<String>) {
    runApplication<TalentlyAppApplication>(*args)
}
