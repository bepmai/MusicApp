package com.mainp.musicapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mainp.musicapp.data.dao.FavoriteSongDao
import com.mainp.musicapp.data.entity.FavoriteSong
import com.mainp.musicapp.data.entity.Song

@Database(
    entities = [FavoriteSong::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteSongDao(): FavoriteSongDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorite_songs_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}