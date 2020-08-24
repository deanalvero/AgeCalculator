package com.lowbottgames.agecalculator.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_table")
data class PersonModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,
        
        @NonNull
        var name: String,
        var day: Int = 1,
        var month: Int = 1,
        var year: Int = 2001,
        var hour: Int = 0,
        var minute: Int = 0
)