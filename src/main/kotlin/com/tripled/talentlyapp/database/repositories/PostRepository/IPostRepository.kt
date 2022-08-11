package com.tripled.talentlyapp.database.repositories.PostRepository

import com.tripled.talentlyapp.core.network.ws.models.response.data.Comment
import com.tripled.talentlyapp.core.network.ws.models.response.data.Post
import com.tripled.talentlyapp.restControllers.request.data.models.PostForm
import com.tripled.talentlyapp.restControllers.response.RestResponse
import org.springframework.web.multipart.MultipartFile

interface IPostRepository {
    fun createPost(post: PostForm, files: Array<MultipartFile>, serverPath: String): RestResponse?
    fun getNewsFeedUntilPostOrReturnAllFeed(postId: String?, userId: String): ArrayList<Post>?
    fun deletePost(postId: String)
    fun addComment(comment: Comment)
    fun addLike(accountId: String, postId: String)
}