package com.lowbottgames.agecalculator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PersonModel::class], version = 1, exportSchema = false)
abstract class PersonDatabase : RoomDatabase() {

    abstract val personDatabaseDao: PersonDatabaseDao

    companion object {

        @Volatile
        private lateinit var INSTANCE: PersonDatabase

        fun getInstance(context: Context): PersonDatabase {
            synchronized(PersonDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        PersonDatabase::class.java,
                        "agecalculator_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

    }
}