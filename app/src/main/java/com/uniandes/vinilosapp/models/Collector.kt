package com.uniandes.vinilosapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_collectors")
data class Collector (
    @PrimaryKey val collectorID:Int,
    val name:String,
    val telephone:String,
    val email:String
)