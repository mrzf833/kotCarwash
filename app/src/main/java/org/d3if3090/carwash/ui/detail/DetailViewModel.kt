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
    private val hasilCarwash = MutableLiveData<HasilCarwash?>()
    fun getDataById(id: Int): LiveData<HistoryEntity> {
        return db.getHistoryById(id)
    }

    fun setCarwash(id: Int){
        val dataHistory = getDataById(id)
        hasilCarwash.value = dataHistory.value?.hasilCarwash()
    }

    fun getHasilCarwas(): LiveData<HasilCarwash?> = hasilCarwash
}