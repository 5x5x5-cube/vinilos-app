package com.uniandes.vinilosapp.database

import androidx.room.TypeConverter
import com.uniandes.vinilosapp.models.GENRE
import com.uniandes.vinilosapp.models.RECORD_LABEL

/**
 * Type converters for Room database to handle enum types
 */
class Converters {
    @TypeConverter
    fun fromGenre(value: GENRE): String = value.toString()
    
    @TypeConverter
    fun toGenre(value: String): GENRE = GENRE.fromString(value)
    
    @TypeConverter
    fun fromRecordLabel(value: RECORD_LABEL): String = value.toString()
    
    @TypeConverter
    fun toRecordLabel(value: String): RECORD_LABEL = RECORD_LABEL.fromString(value)
}