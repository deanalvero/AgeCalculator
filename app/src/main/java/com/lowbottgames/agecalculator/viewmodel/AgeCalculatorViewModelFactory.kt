package com.lowbottgames.agecalculator.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AgeCalculatorViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgeCalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgeCalculatorViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct ViewModel")
    }

}