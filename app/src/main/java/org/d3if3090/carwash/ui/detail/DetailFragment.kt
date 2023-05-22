package org.d3if3090.carwash.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import org.d3if3090.carwash.R
import org.d3if3090.carwash.databinding.FragmentDetailHistoryBinding
import org.d3if3090.carwash.db.CarwashDb
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.db.hasilCarwash
import org.d3if3090.carwash.model.HasilCarwash

class DetailFragment: Fragment() {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by lazy {
        val db = CarwashDb.getInstance(requireContext())
        val factory = DetailViewModelFactory(db.historyDao)
        ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    var historyEntity: HistoryEntity? = null
    private lateinit var binding: FragmentDetailHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailHistoryBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getDataById(args.idHistory).observe(viewLifecycleOwner){
            showDetail(it.hasilCarwash())
            historyEntity = it
        }
        binding.btnBagikan.setOnClickListener { shareData() }
    }

    fun showDetail(history: HasilCarwash?){
        if (history == null) return
        binding.namaSingkatTextView.text = history.nama.substring(0, 1)
        binding.namaKonsumenTextView.text = getString(R.string.nama_submit, history.nama)
        binding.noPolTextView.text = getString(R.string.noPol_submit, history.noPol)
        binding.mobilTextView.text = getString(R.string.mobil_submit, history.mobil)
        binding.jasaTextView.text = getString(R.string.jasa_submit, history.jasa)
        binding.biayaTextView.text = getString(R.string.biaya_submit, history.biaya)
        binding.bayarTextView.text = getString(R.string.bayar_submit, history.bayar.toString())
    }

    private fun shareData(){
        if (historyEntity == null){
            Toast.makeText(this.context, "data tidak ada", Toast.LENGTH_LONG).show()
            return
        }
        true ?: false
        var hasilCarwash = historyEntity?.hasilCarwash()
        val message = "tanggal: ${hasilCarwash?.tanggal}\n" +
                "nama : ${hasilCarwash?.nama}\n" +
                "mobil: ${hasilCarwash?.mobil}\n" +
                "no polisi: ${hasilCarwash?.noPol}\n" +
                "jasa: ${hasilCarwash?.jasa}\n" +
                "biaya: ${hasilCarwash?.biaya}\n" +
                "bayar: ${hasilCarwash?.bayar}\n" +
                "kembalian: ${hasilCarwash?.kembalian}"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }

    }
}