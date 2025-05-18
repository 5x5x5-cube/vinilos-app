package com.uniandes.vinilosapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GENRE {
    CLASSICAL,
    SALSA,
    ROCK,
    FOLK;

    override fun toString(): String {
        return when (this) {
            CLASSICAL -> "Classical"
            SALSA -> "Salsa"
            ROCK -> "Rock"
            FOLK -> "Folk"
        }
    }

    companion object {
        fun fromString(value: String): GENRE {
            return when (value) {
                "Classical" -> CLASSICAL
                "Salsa" -> SALSA
                "Rock" -> ROCK
                "Folk" -> FOLK
                else -> throw IllegalArgumentException("Unknown genre: $value")
            }
        }
    }
}

enum class RECORD_LABEL {
    SONY,
    EMI,
    FUENTES,
    ELEKTRA,
    FANIA;

    override fun toString(): String {
        return when (this) {
            SONY -> "Sony Music"
            EMI -> "EMI"
            FUENTES -> "Discos Fuentes"
            ELEKTRA -> "Elektra"
            FANIA -> "Fania Records"
        }
    }

    companion object {
        fun fromString(value: String): RECORD_LABEL {
            return when (value) {
                "Sony Music" -> SONY
                "EMI" -> EMI
                "Discos Fuentes" -> FUENTES
                "Elektra" -> ELEKTRA
                "Fania Records" -> FANIA
                else -> throw IllegalArgumentException("Unknown record label: $value")
            }
        }
    }
}

@Entity(tableName = "tb_albums")
data class Album(
        @PrimaryKey val albumId: Int,
        val name: String,
        val cover: String,
        val releaseDate: String,
        val description: String,
        val genre: GENRE,
        val recordLabel: RECORD_LABEL,
)
