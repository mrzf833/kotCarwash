package org.d3if3090.carwash.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.d3if3090.carwash.R
import org.d3if3090.carwash.databinding.FragmentDetailHistoryBinding
import org.d3if3090.carwash.db.CarwashDb
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.model.HasilCarwash

class DetailFragment: Fragment() {
    private  val viewModel: DetailViewModel by lazy {
        val db = CarwashDb.getInstance(requireContext())
        val factory = DetailViewModelFactory(db.historyDao)
        ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

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
        viewModel.setCarwash(id)
        viewModel.getHasilCarwas().observe(requireActivity()){
            showDetail(it)
        }
    }

    fun showDetail(history: HasilCarwash?){
        if (history == null) return
        binding.namaKonsumenTextView.text = getString(R.string.nama_submit, history.nama)
        binding.noPolTextView.text = getString(R.string.noPol_submit, history.noPol)
        binding.mobilTextView.text = getString(R.string.mobil_submit, history.mobil)
        binding.jasaTextView.text = getString(R.string.jasa_submit, history.jasa)
        binding.biayaTextView.text = getString(R.string.biaya_submit, history.biaya)
        binding.bayarTextView.text = getString(R.string.bayar_submit, history.bayar.toString())
    }
}