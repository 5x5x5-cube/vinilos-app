package com.uniandes.vinilosapp.repositories

import android.app.Application
import com.uniandes.vinilosapp.models.Collector
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class CollectorRepository(application: Application) {
    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(application)

    suspend fun getCollectors(): List<Collector> {
        return networkServiceAdapter.getCollectors()
    }
}