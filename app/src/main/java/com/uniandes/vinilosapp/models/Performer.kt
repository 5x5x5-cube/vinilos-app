package com.uniandes.vinilosapp.models

enum class PerformerType {
    MUSICIAN,
    BAND
}

data class Performer(
        val performerID: Int,
        val name: String,
        val image: String,
        val description: String,
        val type: PerformerType? = null
)
