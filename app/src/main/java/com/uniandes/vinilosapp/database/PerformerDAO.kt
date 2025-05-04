package com.uniandes.vinilosapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniandes.vinilosapp.models.Performer

@Dao
interface PerformerDao {
    @Query("SELECT * FROM tb_performers")
    fun getPerformers():List<Performer>

    @Query("SELECT * FROM tb_performers WHERE performerID = :id")
    fun getPerformer(id:Int): Performer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(performer: Performer)
}