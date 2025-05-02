package com.uniandes.vinilosapp.repositories

import android.app.Application
import com.uniandes.vinilosapp.models.Band
import com.uniandes.vinilosapp.models.Musician
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.models.PerformerType
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class PerformerRepository(val application: Application) {
    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(application)

    suspend fun getAllPerformers(): List<Performer> {
        val musicians = networkServiceAdapter.getMusicians()
        val bands = networkServiceAdapter.getBands()

        val performerMusicians =
                musicians.map { musician ->
                    Performer(
                            performerID = musician.performerID,
                            name = musician.name,
                            image = musician.image,
                            description = musician.description,
                            type = PerformerType.MUSICIAN
                    )
                }

        val performerBands =
                bands.map { band ->
                    Performer(
                            performerID = band.performerID,
                            name = band.name,
                            image = band.image,
                            description = band.description,
                            type = PerformerType.BAND
                    )
                }

        return performerMusicians + performerBands
    }

    suspend fun getMusician(musicianId: Int): Musician {
        return networkServiceAdapter.getMusician(musicianId)
    }

    suspend fun getBand(bandId: Int): Band {
        return networkServiceAdapter.getBand(bandId)
    }

    suspend fun getPerformer(performerId: Int, type: PerformerType? = null): Performer {
        if (type != null) {
            return when (type) {
                PerformerType.MUSICIAN -> {
                    val musician = getMusician(performerId)
                    Performer(
                            performerId,
                            musician.name,
                            musician.image,
                            musician.description,
                            PerformerType.MUSICIAN
                    )
                }
                PerformerType.BAND -> {
                    val band = getBand(performerId)
                    Performer(
                            performerId,
                            band.name,
                            band.image,
                            band.description,
                            PerformerType.BAND
                    )
                }
            }
        } else {
            val performers = getAllPerformers()
            return performers.find { it.performerID == performerId }
                    ?: throw Exception("Performer with ID $performerId not found")
        }
    }
}
