package com.uniandes.vinilosapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniandes.vinilosapp.models.Collector

@Dao
interface CollectorDao {

    @Query("SELECT * FROM tb_collectors")
    fun getCollectors():List<Collector>

    @Query("SELECT * FROM tb_collectors WHERE collectorID = :id")
    fun getCollector(id:Int): Collector

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(collector: Collector)

    @Query("DELETE FROM tb_collectors")
    fun deleteAll(): Int
}