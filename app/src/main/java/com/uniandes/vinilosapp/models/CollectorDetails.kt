package com.uniandes.vinilosapp.models

data class CollectorDetails (
    val collectorID:Int,
    val name:String,
    val telephone: String,
    val email:String,
    val performers: List<Performer>
)