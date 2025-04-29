package com.uniandes.vinilosapp.repositories

import android.app.Application
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class PerformerRepository(application: Application) {
    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(application)

    suspend fun getAllPerformers(): List<Performer> {
        val musicians = networkServiceAdapter.getMusicians()
        val bands = networkServiceAdapter.getBands()
        return musicians + bands
    }
}