package com.uniandes.vinilosapp.models

data class Band(
    val performerID: Int,
    val name: String,
    val image: String,
    val description: String,
    val creationDate: String,
    val musicians: List<Musician> = emptyList()
)