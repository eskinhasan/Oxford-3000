package com.example.artbooktesting.model

data class ImageResponse(
    val hits:List<ImageResult>,
    val total:Int,
    val totalHists: Int
) {
}