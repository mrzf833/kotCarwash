package org.d3if3090.carwash.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3090.carwash.db.HistoryDao

class DetailViewModelFactory(
    private val db: HistoryDao
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(db) as T
        }
        throw java.lang.IllegalArgumentException("Uknown ViewModel class")
    }
}