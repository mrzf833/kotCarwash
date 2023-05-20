package org.d3if3090.carwash.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if3090.carwash.R
import org.d3if3090.carwash.databinding.ItemHistoryBinding
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.db.hasilCarwash
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK){
    companion object{
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<HistoryEntity>(){
                override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    class ViewHolder(
        private val binding: ItemHistoryBinding
    ): RecyclerView.ViewHolder(binding.root){
        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        fun bind(item: HistoryEntity) = with(binding){
            val hasilCarwash = item.hasilCarwash()
            namaSingkatTextView.text = hasilCarwash.nama.substring(0, 1)
            tanggalTextView.text = dateFormatter.format(Date(item.tanggal))
            namaKonsumenTextView.text = "nama: " + hasilCarwash.nama
            namaMobilTextView.text = "mobil: " + hasilCarwash.mobil
            biayaTextView.text = "biaya: " +  hasilCarwash.biaya


            // disini bisa
            btnDelete.setOnClickListener {
                Log.d("id", "ID = "+ item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}