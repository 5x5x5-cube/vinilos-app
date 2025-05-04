package com.uniandes.vinilosapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PerformerType {
    MUSICIAN,
    BAND
}


@Entity(tableName = "tb_performers")
data class Performer (
    @PrimaryKey val performerID:Int,
    val name: String,
    val image: String,
    val description: String,
    val type: PerformerType? = null

    )
