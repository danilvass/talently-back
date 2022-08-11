package com.tripled.talentlyapp.database.Mappers


import com.tripled.talentlyapp.core.network.ws.models.response.data.Comment
import com.tripled.talentlyapp.core.network.ws.models.response.data.PostForFeedSql
import org.apache.ibatis.annotations.*
import org.apache.ibatis.mapping.StatementType

interface PostMapper {
    @Insert("Insert into post (id, portfolio_id, description, content_type) Select #{postId}, #{portfolioId},#{description},content_type.id FROM content_type Where type = #{contentType}")
    fun createPost(postId: String, description: String?, portfolioId: String?, contentType: String)

    @Insert("Insert into photo_content(id, content_path, post_id) Values (#{id}, #{contentPath}, #{postId})")
    fun addPhotoContent(id: String, contentPath: String, postId: String)

    @Insert("Insert into video_content(id, content_path, post_id) Values (#{id}, #{contentPath}, #{postId})")
    fun addVideoContent(id: String, contentPath: String, postId: String)

    @Delete("delete from post " +
            "where id = #{postId}")
    fun deletePost(postId: String)

    @Select("call getNewsFeedUntilPost(#{postId}, #{userId})")
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(ArrayList::class)
    fun getNewsFeedUntilPost(postId: String?, userId: String): ArrayList<PostForFeedSql>?


    @Select("Select" +
            "   #{postId}," +
            "   login," +
            "   pc.account_id," +
            "   text" +
            "   From post_comments pc" +
            "       Left join account acc" +
            "       on acc.account_id = pc.account_id" +
            "   Where pc.post_id = #{postId}")
    fun getAllCommentsByPostId(postId: String): ArrayList<Comment>

    @Insert( "Insert into post_comments (id, account_id, post_id, text) Values ((Select UUID()), #{accountId}, #{postId}, #{text} )")
    fun addComment(accountId: String?, postId: String, text: String)

    @Insert("call addLike(#{accountId},#{postId})")
    fun addLike(accountId: String, postId: String)




}