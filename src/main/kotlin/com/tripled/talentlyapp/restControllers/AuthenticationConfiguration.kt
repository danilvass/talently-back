package com.tripled.talentlyapp.restControllers

import com.tripled.talentlyapp.TalentlyAppApplication.Companion.userCredentialsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.lang.Exception
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class AuthenticationConfiguration : WebMvcConfigurer {
    @Autowired
    private val interceptor = RequestInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor).addPathPatterns("/rest/*")
    }
}


@Component
class RequestInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val userId = request.getHeader("userId")
            val token = request.getHeader("authToken")
            if (userId != null && token != null && !userCredentialsRepository.checkIfUserTokenIsValid(userId, token).isNullOrEmpty()) {

                return super.preHandle(request, response, handler)
            } else {
                response.writer.write("User is not authorized ")
                response.status = 400
                return false
            }
        } catch (e: Exception) {
            response.writer.write(e.message)
            response.status = 400
            return false
        }
    }
}