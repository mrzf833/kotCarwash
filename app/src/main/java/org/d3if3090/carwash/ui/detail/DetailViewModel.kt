package org.d3if3090.carwash.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if3090.carwash.db.HistoryDao
import org.d3if3090.carwash.db.HistoryEntity

class DetailViewModel (private val db: HistoryDao) : ViewModel() {
    fun getDataById(id: Long): LiveData<HistoryEntity> {
        val dataEntity = db.getHistoryById(id)
        return dataEntity
    }
}