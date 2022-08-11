package com.tripled.talentlyapp.restControllers.request.data.models

import com.tripled.talentlyapp.restControllers.request.data.enum.PostContentType


data class PostForm(val portfolioId: String?, val contentType: PostContentType, val description: String?)