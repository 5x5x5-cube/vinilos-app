package com.uniandes.vinilosapp.models

data class Musician(
    val performerID: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val band: Band? = null
)