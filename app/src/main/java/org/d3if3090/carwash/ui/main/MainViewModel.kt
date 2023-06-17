package org.d3if3090.carwash.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3090.carwash.db.HistoryDao
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.db.hasilCarwash
import org.d3if3090.carwash.model.DataTipeJasa
import org.d3if3090.carwash.model.HasilCarwash
import org.d3if3090.carwash.model.TipeJasa
import org.d3if3090.carwash.network.ApiStatus
import org.d3if3090.carwash.network.TipeJasaApi
import org.d3if3090.carwash.network.UpdateWorker
import java.util.concurrent.TimeUnit

class MainViewModel(private val db: HistoryDao) : ViewModel() {
    val dataLastHistory = db.getLastHistoryData()
    private val hasilCarwash = MutableLiveData<HasilCarwash?>()
    private val history = MutableLiveData<HistoryEntity?>()
    private val navigasiToDetail = MutableLiveData<Long?>()
    private var dataTipeJasa = DataTipeJasa().getData()


    fun setNullCarwash(){
        hasilCarwash.value = null
    }
    fun setCarwash(nama: String, mobil: String,noPol: String, jasa: String, bayar: Int){
        val tipeJasa = getTipeJasa(jasa)
        val kembalian = bayar - tipeJasa.biaya

        val dataHistory = HistoryEntity(
            namaKonsumen = nama,
            namaMobil = mobil,
            jasa = jasa,
            biaya = tipeJasa.totalBiaya,
            kembalian = kembalian,
            noPol = noPol,
            bayar = bayar
        )
        hasilCarwash.value = dataHistory.hasilCarwash()
        history.value = dataHistory
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.insert(dataHistory)
            }
        }
    }

    fun getTipeJasa(tipe_jasa: String): TipeJasa {
        var tipeJasa: TipeJasa = TipeJasa(
            "", 0, ""
        )

        val datas = dataTipeJasa.value!!

        for (data in datas){
            if(data.nama == tipe_jasa){
                tipeJasa = data
                break
            }
        }
        return  tipeJasa
    }

    fun getHasilCarwas(): LiveData<HasilCarwash?> = hasilCarwash

    fun mulaiNavigasiToDetail(idHistory: Long){
        navigasiToDetail.value = idHistory
    }

    fun selesaiNavigasiToDetail(){
        navigasiToDetail.value = null
    }

    fun getNavigasiToDetail(): LiveData<Long?> = navigasiToDetail

    fun getDataTipeJasa() = dataTipeJasa

    fun dataTipeJasaOnlyNama(): List<String>{
        return dataTipeJasa.value!!.map {
            it.nama
        }
    }

    fun getStatusTipeJasa(): LiveData<ApiStatus> = DataTipeJasa().getStatus()

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}