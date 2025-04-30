package com.uniandes.vinilosapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uniandes.vinilosapp.models.Album


@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class VinilosDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: VinilosDatabase? = null

        fun getDatabase(context: Context): VinilosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinilosDatabase::class.java,
                    "vinilos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}