package com.example.composenetworkrequest.api.schema

data class Music(
    val id: Int,
    val name: String,
    val metadata: MetaData
)

data class MetaData(
    val description: String,
)