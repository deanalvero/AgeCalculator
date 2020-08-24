package com.lowbottgames.agecalculator.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDatabaseDao {

    @Insert
    fun insert(person: PersonModel)

    @Delete
    fun delete(person: PersonModel)

    @Update
    fun update(person: PersonModel)

    @Query("SELECT * FROM person_table where id = :id LIMIT 1")
    fun loadPersonById(id: Long) : LiveData<PersonModel>

    @Query("SELECT * FROM person_table")
    fun getAllPersons() : LiveData<List<PersonModel>>

}