package org.d3if3090.carwash.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3090.carwash.db.HistoryDao

class HistoryViewModel(private val db: HistoryDao) : ViewModel() {
    val data = db.getLastHistory()

    fun hapusSemuaData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.deleteAllHistory()
        }
    }

    fun hapusData(id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.deleteHistory(id)
        }
    }
}