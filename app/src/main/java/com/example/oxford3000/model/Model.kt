package com.example.oxford3000.model

import kotlinx.serialization.Serializable


@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class Word(
    val id: String,
    var categoryId:String,
    var eng:String,
    var tr:String
)

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class Sentences(
    val id: String,
    var categoryId:String,
    var engSentences:String,
    var trSentences:String

)


@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class SentencesWord(
    val eng: String,
    val tr: String
)

