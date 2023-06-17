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
import org.d3if3090.carwash.network.ApiStatus
import org.d3if3090.carwash.network.TipeJasaApi

class DataTipeJasa {
    init {
        retrieveDataTipeJasa()
    }

    val datas = MutableLiveData<List<TipeJasa>>()

    private val status = MutableLiveData<ApiStatus>()
    fun getData():LiveData<List<TipeJasa>>{
        return datas
    }

    fun getStatus(): LiveData<ApiStatus> = status

    private fun retrieveDataTipeJasa(){
        CoroutineScope(Job() + Dispatchers.Main).launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                val result = TipeJasaApi.service.getTipeJasa()
                datas.postValue(result)
                status.postValue(ApiStatus.SUCCESS)
                Log.d("tipe jasa", "Success Get Tipe Jasa")
            }catch (e: Exception){
                Log.d("tipe jasa", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

}