package org.d3if3090.carwash.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3090.carwash.db.HistoryDao

class MainViewModelFactory(
    private val db: HistoryDao
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw java.lang.IllegalArgumentException("Uknown ViewModel class")
    }
}