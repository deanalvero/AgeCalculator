package com.lowbottgames.agecalculator.repository

import androidx.lifecycle.LiveData
import com.lowbottgames.agecalculator.database.PersonDatabase
import com.lowbottgames.agecalculator.database.PersonModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonRepository(private val database: PersonDatabase) {

    private val personDatabaseDao = database.personDatabaseDao
    val persons = personDatabaseDao.getAllPersons()

    suspend fun insert(person: PersonModel) {
        withContext(Dispatchers.IO) {
            personDatabaseDao.insert(person)
        }
    }

    suspend fun update(person: PersonModel) {
        withContext(Dispatchers.IO) {
            personDatabaseDao.update(person)
        }
    }

    suspend fun delete(person: PersonModel) {
        withContext(Dispatchers.IO) {
            personDatabaseDao.delete(person)
        }
    }

    fun loadPersonById(id: Long) : LiveData<PersonModel> {
        return personDatabaseDao.loadPersonById(id)
    }

}