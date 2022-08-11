package com.tripled.talentlyapp.restControllers.request.data.models

import org.springframework.web.multipart.MultipartFile


data class PortfolioForm(val description: String? , val category: String, val name: String)