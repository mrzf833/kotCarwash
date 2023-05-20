package org.d3if3090.carwash.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import org.d3if3090.carwash.R
import org.d3if3090.carwash.databinding.FragmentMainBinding
import org.d3if3090.carwash.db.CarwashDb
import org.d3if3090.carwash.model.DataTipeJasa
import org.d3if3090.carwash.model.HasilCarwash

class MainFragment: Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        val db = CarwashDb.getInstance(requireContext())
        val factory = MainViewModelFactory(db.historyDao)
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private var biaya: Int = 0

    private  var jasa: String = ""

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewModel.getHasilCarwas().observe(this){
//            showResult(it)
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getHasilCarwas().observe(requireActivity()){
            showResult(it)
        }

        binding.btnDetail.setOnClickListener {
            viewModel.mulaiNavigasiToDetail()
        }

        viewModel.getNavigasiToDetail().observe(viewLifecycleOwner){
            if(it == null) return@observe
            findNavController().navigate(MainFragmentDirections
                .actionMainFragmentToHistoryFragment(it))
            viewModel.selesaiNavigasiToDetail()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        val spinner: Spinner = binding.tipeJasaSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.tipe_jasa_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        binding.btnSubmit.setOnClickListener{
            submit()
        }

        binding.btnReset.setOnClickListener{
            reset()
        }

        spinner.selected {  }

        return binding.root
    }


    fun Spinner.selected(action: (position:Int) -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action(position)
                val tipe_jasa = parent?.getItemAtPosition(position).toString()
                jasa = tipe_jasa
                val totalBiaya: String

                val getTipeJasa = viewModel.getTipeJasa(tipe_jasa)
                biaya = getTipeJasa.biaya
                totalBiaya = getTipeJasa.totalBiaya
                showTotalBiaya(totalBiaya)
            }
        }
    }

    private fun showTotalBiaya(totalBiaya: String){
        binding.totalBiayaInp.setText(totalBiaya)
    }

    fun submit(){
        val namaKonsumen = binding.namaKonsumenInp.text.toString()
        if (TextUtils.isEmpty(namaKonsumen)) {
            Toast.makeText(this.context, R.string.nama_invalid, Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(jasa)) {
            Toast.makeText(this.context, R.string.jasa_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val noPol = binding.noPolInp.text.toString()
        if (TextUtils.isEmpty(noPol)) {
            Toast.makeText(this.context, R.string.nopol_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val bayarInp: Int
        try {
            val bayarCheck = binding.bayarInp.text.toString()
            if (TextUtils.isEmpty(bayarCheck)) {
                Toast.makeText(this.context, R.string.bayar_invalid, Toast.LENGTH_LONG).show()
                return
            }

            bayarInp = bayarCheck.toInt()
        }catch (ex: NumberFormatException){
            Toast.makeText(this.context, R.string.bayar_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val namaMobil = binding.namaMobilInp.text.toString()
        if (TextUtils.isEmpty(namaMobil)) {
            Toast.makeText(this.context, R.string.mobil_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val biayaInpText = binding.totalBiayaInp.text.toString()
        if (TextUtils.isEmpty(biayaInpText)) {
            Toast.makeText(this.context, R.string.biaya_invalid, Toast.LENGTH_LONG).show()
            return
        }
        if(bayarInp - biaya < 0){
            Toast.makeText(this.context, R.string.bayar_kurang, Toast.LENGTH_LONG).show()
            return
        }
        viewModel.setCarwash(namaKonsumen, namaMobil,noPol, jasa, bayarInp)
        Toast.makeText(this.context, R.string.submit_berhasil, Toast.LENGTH_LONG).show()
    }

    private fun showResult(result: HasilCarwash?){
        if (result == null) return
        binding.txtNamaSubmit.text = getString(R.string.nama_submit, result.nama)
        binding.txtJasaSubmit.text = getString(R.string.jasa_submit, result.jasa)
        binding.txtMobilSubmit.text = getString(R.string.mobil_submit, result.mobil)
        binding.txtBiayaSubmit.text = getString(R.string.biaya_submit, result.biaya)
        binding.txtKembalianSubmit.text = getString(R.string.kembalian_submit, result.kembalian.toString())

        tampilkanSubmit()
    }


    fun tampilkanSubmit(){
        binding.btnDetail.setVisibility(View.VISIBLE)
        binding.txtNamaSubmit.setVisibility(View.VISIBLE)
        binding.txtJasaSubmit.setVisibility(View.VISIBLE)
        binding.txtMobilSubmit.setVisibility(View.VISIBLE)
        binding.txtBiayaSubmit.setVisibility(View.VISIBLE)
        binding.txtKembalianSubmit.setVisibility(View.VISIBLE)
    }

    fun reset(){
        binding.btnDetail.setVisibility(View.GONE)
        binding.txtNamaSubmit.setVisibility(View.GONE)
        binding.txtJasaSubmit.setVisibility(View.GONE)
        binding.txtMobilSubmit.setVisibility(View.GONE)
        binding.txtBiayaSubmit.setVisibility(View.GONE)
        binding.txtKembalianSubmit.setVisibility(View.GONE)

        binding.namaKonsumenInp.setText("")
        binding.noPolInp.setText("")
        binding.namaMobilInp.setText("")
        binding.bayarInp.setText("")

        Toast.makeText(this.context, R.string.reset_berhasil, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_about -> {
                findNavController().navigate(
                    R.id.action_mainFragment_to_aboutFragment)
                return true
            }
            R.id.menu_histori -> {
                findNavController().navigate(
                    R.id.action_mainFragment_to_historyFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}