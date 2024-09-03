package com.example.oxford3000.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
@SerializedName("hits")
val hits: List<ImageResult>,
@SerializedName("total")
val total: Int,
@SerializedName("totalHits")
val totalHits: Int
)
