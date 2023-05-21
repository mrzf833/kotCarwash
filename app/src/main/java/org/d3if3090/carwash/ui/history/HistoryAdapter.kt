package org.d3if3090.carwash.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3090.carwash.R
import org.d3if3090.carwash.databinding.ItemHistoryBinding
import org.d3if3090.carwash.db.CarwashDb
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.db.hasilCarwash
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(val fragment: Fragment): ListAdapter<HistoryEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK){

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
        private val navigasiToDetail = MutableLiveData<Long?>()
        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        fun bind(item: HistoryEntity, view: View, fragment: Fragment) = with(binding){
            val hasilCarwash = item.hasilCarwash()
            namaSingkatTextView.text = hasilCarwash.nama.substring(0, 1)
            tanggalTextView.text = dateFormatter.format(Date(item.tanggal))
            namaKonsumenTextView.text = "nama: " + hasilCarwash.nama
            namaMobilTextView.text = "mobil: " + hasilCarwash.mobil
            biayaTextView.text = "biaya: " +  hasilCarwash.biaya
            binding.btnDelete.setOnClickListener {
                hapusData(item.id, view.context)
            }
            binding.btnDetail.setOnClickListener {
                mulaiNavigasiToDetail(item.id)
            }
            getNavigasiToDetail().observe(fragment.viewLifecycleOwner){
                if(it == null) return@observe
                fragment.findNavController().navigate(
                    HistoryFragmentDirections
                    .actionHistoryFragmentToDetailFragment(it))
                selesaiNavigasiToDetail()
            }
        }

        private fun hapusData(id: Long, context: Context){
            val db = CarwashDb.getInstance(context)
            val historyDao = db.historyDao
            MaterialAlertDialogBuilder(context)
                .setMessage(context.getString(R.string.konfirmasi_hapus_satu, id.toString()))
                .setPositiveButton(context.getString(R.string.hapus)) {_, _ ->
                    CoroutineScope(Dispatchers.IO).launch{
                        withContext(Dispatchers.IO) {
                            historyDao.deleteHistory(id)
                        }
                    }
                }
                .setNegativeButton(context.getString(R.string.batal)) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }

        fun mulaiNavigasiToDetail(idHistory: Long){
            navigasiToDetail.value = idHistory
        }

        fun selesaiNavigasiToDetail(){
            navigasiToDetail.value = null
        }

        fun getNavigasiToDetail(): LiveData<Long?> = navigasiToDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        // disini bisa
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView, fragment)
    }


}