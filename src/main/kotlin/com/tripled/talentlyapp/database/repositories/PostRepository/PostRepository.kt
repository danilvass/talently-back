package com.tripled.talentlyapp.database.repositories.PostRepository

import com.tripled.talentlyapp.TalentlyAppApplication
import com.tripled.talentlyapp.core.network.ws.models.response.data.*
import com.tripled.talentlyapp.core.network.ws.models.response.data.dataEnums.ErrorType
import com.tripled.talentlyapp.database.Mappers.PostMapper
import com.tripled.talentlyapp.database.Mappers.ProfileDataMapper
import com.tripled.talentlyapp.restControllers.request.data.enum.PostContentType
import com.tripled.talentlyapp.restControllers.request.data.models.PostForm
import com.tripled.talentlyapp.restControllers.response.RestResponse
import com.tripled.talentlyapp.utils.sessionCloseAndCommit
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class PostRepository : IPostRepository {

    private var serverPath: String? = null

    override fun addComment(comment: Comment) {
        val session = TalentlyAppApplication.dbConnection.openSession()
        val postMapper = session.getMapper(PostMapper::class.java)
        postMapper.addComment(comment.accountId, comment.postId, comment.text)
        sessionCloseAndCommit(session)
    }

    override fun addLike(accountId: String, postId: String) {
        val session = TalentlyAppApplication.dbConnection.openSession()
        val postMapper = session.getMapper(PostMapper::class.java)
        postMapper.addLike(accountId, postId)
        sessionCloseAndCommit(session)
    }

    override fun getNewsFeedUntilPostOrReturnAllFeed(postId: String?, userId: String): ArrayList<Post>? {
        val session = TalentlyAppApplication.dbConnection.openSession()
        val postMapper = session.getMapper(PostMapper::class.java)
        val bigPostData = postMapper.getNewsFeedUntilPost(postId, userId)
        val posts = getCommentsIntoPosts(getDataFromPostSqlModelToPostModel(bigPostData), postMapper)
        sessionCloseAndCommit(session)
        return posts

    }

    private fun getCommentsIntoPosts(posts: ArrayList<Post>?, mapper: PostMapper): ArrayList<Post>? {
        posts?.forEach {
            it.comments = mapper.getAllCommentsByPostId(it.id)
        }
        return posts
    }

    private fun getDataFromPostSqlModelToPostModel(postSqlModels: ArrayList<PostForFeedSql>?): ArrayList<Post>? {
        var posts = arrayListOf<Post>()
        postSqlModels?.forEach {
            val pffs: PostForFeedSql = it
            val index = posts.indexOfFirst { it.id == pffs.id }
            if (index != -1) {
                posts[index].postData?.add(pffs.postData)
            } else {
                var list = arrayListOf<String>()
                list.add(pffs.postData)
                posts.add(Post(pffs.id, pffs.type, pffs.description, list, arrayListOf(), pffs.accountId, pffs.portfolioId, pffs.userAvatarUrl, pffs.accountName))
            }
        }
        return posts
    }

    override fun createPost(post: PostForm, files: Array<MultipartFile>, serverPath: String): RestResponse? {
        this.serverPath = serverPath
        var response: RestResponse? = null
        val generatedPostId: String = UUID.randomUUID().toString()
        val session = TalentlyAppApplication.dbConnection.openSession()
        val postMapper = session.getMapper(PostMapper::class.java)
        postMapper.createPost(generatedPostId, post.description, post.portfolioId, post.contentType.toString())


        files.forEach {
            when (it.contentType.toLowerCase()) {
                "image/jpeg" -> {
                    if (post.contentType == PostContentType.IMAGE || post.contentType == PostContentType.IMAGE_COLLECTION) {
                        response = addPostImage(generatedPostId, it, postMapper)
                    } else {
                        return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "Wrong file type", "Wrong file type"))
                    }
                }
                "video/mp4", "video/quicktime" -> {
                    if (post.contentType == PostContentType.VIDEO) {
                        response = addPostVideo(generatedPostId, it, postMapper)
                    } else {

                        return RestResponse(null, ErrorResponseData(ErrorType.BUSINESS_ERROR, "Wrong file type", "Wrong file type"))
                    }
                }
            }
        }
        if (response == null) {
            sessionCloseAndCommit(session)
        } else {
            session.close()
        }
        return null
    }

    override fun deletePost(postId: String) {
        val session = TalentlyAppApplication.dbConnection.openSession()
        val postMapper = session.getMapper(PostMapper::class.java)
        postMapper.deletePost(postId)
        sessionCloseAndCommit(session)
    }

    private fun addPostImage(postId: String, file: MultipartFile, mapper: PostMapper): RestResponse? {
        val generatedVideoId: String = UUID.randomUUID().toString()
        var fileName = "imageContent/" + RandomStringUtils.randomAlphanumeric(8) + postId + ".jpg"
        file.transferTo(File("$serverPath$fileName"))
        mapper.addPhotoContent(generatedVideoId, "http://45.149.128.107:8081/$fileName", postId)
        return null
    }


    private fun addPostVideo(postId: String, file: MultipartFile, mapper: PostMapper): RestResponse? {
        val generatedVideoId: String = UUID.randomUUID().toString()
        var fileName = "videoContent/" + RandomStringUtils.randomAlphanumeric(8) + postId
        when (file.contentType.toLowerCase()) {
            "video/mp4" -> fileName += ".mp4"
            "video/quicktime" -> fileName += ".mov"
        }
        file.transferTo(File("$serverPath$fileName"))
        mapper.addVideoContent(generatedVideoId, "http://45.149.128.107:8081/$fileName", postId)
        return null
    }


}