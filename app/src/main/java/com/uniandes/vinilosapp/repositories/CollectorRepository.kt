package com.uniandes.vinilosapp.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.uniandes.vinilosapp.database.CollectorDao
import com.uniandes.vinilosapp.models.Collector
import com.uniandes.vinilosapp.models.CollectorDetails
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class CollectorRepository(val application: Application, private val collectorsDao: CollectorDao) {

    suspend fun getCollectors(): List<Collector> {
        var cached = collectorsDao.getCollectors()
        return if (cached.isNullOrEmpty()) {
            val cm =
                    application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                            ConnectivityManager
            if (cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI &&
                            cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE
            ) {
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getCollectors()
        } else cached
    }

    suspend fun getCollectorDetail(collectorId: Int): CollectorDetails {

        return NetworkServiceAdapter.getInstance(application).getCollector(collectorId)
    }
}
