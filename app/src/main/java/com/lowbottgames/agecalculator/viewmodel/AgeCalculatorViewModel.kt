package com.lowbottgames.agecalculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lowbottgames.agecalculator.database.PersonDatabase
import com.lowbottgames.agecalculator.database.PersonModel
import com.lowbottgames.agecalculator.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AgeCalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(PersonDatabase.getInstance(application))

    val persons = personRepository.persons

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun insert(person: PersonModel) {
        viewModelScope.launch {
            personRepository.insert(person)
        }
    }

    fun update(person: PersonModel) {
        viewModelScope.launch {
            personRepository.update(person)
        }
    }

    fun delete(person: PersonModel) {
        viewModelScope.launch {
            personRepository.delete(person)
        }
    }

    fun loadPersonById(id: Long) : LiveData<PersonModel> {
        return personRepository.loadPersonById(id)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}