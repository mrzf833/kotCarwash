package org.d3if3090.carwash.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3090.carwash.db.HistoryDao
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.db.hasilCarwash
import org.d3if3090.carwash.model.HasilCarwash

class DetailViewModel (private val db: HistoryDao) : ViewModel() {
    fun getDataById(id: Long): LiveData<HistoryEntity> {
        return db.getHistoryById(id)
    }
}