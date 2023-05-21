package org.d3if3090.carwash.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.d3if3090.carwash.model.HasilCarwash
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var tanggal: Long = System.currentTimeMillis(),
    var namaKonsumen: String,
    var namaMobil: String,
    val noPol: String,
    var jasa: String,
    var biaya: String,
    var kembalian: Int,
    val bayar: Int

)

fun HistoryEntity.hasilCarwash(): HasilCarwash {
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy")
    val tanggal = dateFormatter.format(Date(tanggal))
    return HasilCarwash(namaKonsumen, namaMobil,noPol, jasa, biaya, kembalian, bayar, tanggal)
}