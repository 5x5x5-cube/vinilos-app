package com.uniandes.vinilosapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.Collector
import com.uniandes.vinilosapp.models.Performer

@Database(
        entities = [Album::class, Performer::class, Collector::class],
        version = 3,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VinilosDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumDao
    abstract fun performerDao(): PerformerDao
    abstract fun collectorDao(): CollectorDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile private var INSTANCE: VinilosDatabase? = null

        fun getDatabase(context: Context): VinilosDatabase {
            return INSTANCE
                    ?: synchronized(this) {
                        val instance =
                                Room.databaseBuilder(
                                                context.applicationContext,
                                                VinilosDatabase::class.java,
                                                "vinilos_database"
                                        )
                                        .fallbackToDestructiveMigration()
                                        .build()
                        INSTANCE = instance
                        instance
                    }
        }
    }
}
