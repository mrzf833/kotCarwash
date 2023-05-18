package org.d3if3090.carwash.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3090.carwash.model.DataTipeJasa
import org.d3if3090.carwash.model.HasilCarwash
import org.d3if3090.carwash.model.TipeJasa

class MainViewModel : ViewModel() {
    private val hasilCarwash = MutableLiveData<HasilCarwash?>()
    fun getCarwash(nama: String, mobil: String, jasa: String, bayar: Int){
        val tipeJasa = getTipeJasa(jasa)
        val kembalian = bayar - tipeJasa.biaya
        hasilCarwash.value = HasilCarwash(nama, mobil, tipeJasa.nama, tipeJasa.totalBiaya, kembalian)
    }

    fun getTipeJasa(tipe_jasa: String): TipeJasa {
        var tipeJasa: TipeJasa = TipeJasa(
            "", 0, ""
        )

        val datas = DataTipeJasa().getData()

        for (data in datas){
            if(data.nama == tipe_jasa){
                tipeJasa = data
                break
            }
        }
        return  tipeJasa
    }

    fun getHasilCarwas(): LiveData<HasilCarwash?> = hasilCarwash
}