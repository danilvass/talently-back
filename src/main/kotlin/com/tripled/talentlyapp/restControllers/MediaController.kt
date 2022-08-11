package com.tripled.talentlyapp.restControllers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.mediaRepository
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.portfolioRepository
import com.tripled.talentlyapp.TalentlyAppApplication.Companion.postRepository
import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.restControllers.response.RestResponse
import com.tripled.talentlyapp.restControllers.response.data.UploadAvatarResponseData
import com.tripled.talentlyapp.restControllers.request.data.models.PortfolioForm
import com.tripled.talentlyapp.restControllers.request.data.models.PostForm
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File


@RestController
@RequestMapping("rest")
class MediaController {

    @Value("\${avatar.images.avatar.url}")
    private val avatarUrl: String? = null

    @Value("\${files.server.path}")
    private val serverPath: String? = null
    private val mapper = jacksonObjectMapper()

    @PostMapping("/uploadAvatar")
    fun uploadAvatar(@RequestBody image: MultipartFile?, @RequestHeader(value = "userId") userId: String): RestResponse {
        if (image != null) {
            try {
                val fileName = RandomStringUtils.randomAlphanumeric(8) + userId + ".jpg"
                val oldAvatarImage = mediaRepository.getAvatarImageNameByUserId(userId)
                mediaRepository.addAvatarImagePath(userId, avatarUrl+fileName)
                image.transferTo(File(serverPath+"avatarImages/"+fileName))
                //TODO("Make delete file after response")
                if (!oldAvatarImage.isNullOrEmpty()) {
                    val file = File(serverPath+"avatarImages/"+oldAvatarImage)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                return RestResponse(UploadAvatarResponseData("$avatarUrl$fileName"))
            } catch (e: Exception) {
                return RestResponse(null, ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.stackTrace.toString(), e.message.toString()))
            }
        } else {
            return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "File is empty", "Send to controller empty file"))
        }
    }

    @PostMapping("/createPortfolio")
    @ResponseBody
    fun createPortfolio(@RequestParam image: MultipartFile?,
                        @RequestParam info: String?,
                        @RequestHeader(value = "userId") userId: String): RestResponse? {
        val json: JsonNode = mapper.readTree(info)
        val portfolio = mapper.treeToValue(json, PortfolioForm::class.java)
        if (portfolio != null) {
            if (image != null) {
                try {
                    val fileName = "portfolioAvatarImage/" + RandomStringUtils.randomAlphanumeric(8) + image.name + ".jpg"
                    image.transferTo(File("$serverPath$fileName"))
                    portfolioRepository.createPortfolio(userId = userId, name = portfolio.name, filename = "http://45.149.128.107:8081/$fileName", description = portfolio.description, category = portfolio.category)
                    return null
                } catch (e: Exception) {
                    return RestResponse(null, ErrorResponseData(ErrorType.INTERNAL_SERVER_ERROR, e.message.toString(), e.printStackTrace().toString()))
                }
            } else {
                return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "File is empty", "Send to controller empty file"))
            }
        } else {
            return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "Portfolio data is empty", "Send to controller empty portfolio form"))
        }

    }


    @PostMapping("/createPost")
    fun createPost(@RequestParam files: Array<MultipartFile>,
                   @RequestParam info: String?,
                   @RequestHeader(value = "userId") userId: String): RestResponse? {
        val json: JsonNode = mapper.readTree(info)
        val post = mapper.treeToValue(json, PostForm::class.java)
        if (post != null) {
            if (files.size != 0) {
                return serverPath?.let { postRepository.createPost(post, files, it) }
            } else {
                return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "Missing files", "Missing files in post"))
            }
        } else {
            return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "Post data is empty or incorrect", "Send to controller empty or incorrect post form"))
        }
    }
}