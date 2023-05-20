package org.d3if3090.carwash.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3090.carwash.db.HistoryDao

class HistoryViewModelFactory(
    private val db: HistoryDao
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(db) as T
        }

        throw IllegalAccessException("Uknown ViewModel class")
    }
}