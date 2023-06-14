package org.d3if3090.carwash.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.d3if3090.carwash.network.TipeJasaApi

class DataTipeJasa {
    init {
        retrieveDataTipeJasa()
    }

    val datas = MutableLiveData<List<TipeJasa>>()
    fun getData():LiveData<List<TipeJasa>>{
        return datas
    }

    private fun retrieveDataTipeJasa(){
        CoroutineScope(Job() + Dispatchers.Main).launch(Dispatchers.IO) {
            try {
                val result = TipeJasaApi.service.getTipeJasa()
                datas.postValue(result)
                Log.d("tipe jasa", "success: ${result}")
                Log.d("tipe jasa", "selesai")
            }catch (e: Exception){
                Log.d("tipe jasa", "Failure: ${e.message}")
            }
        }
    }

}