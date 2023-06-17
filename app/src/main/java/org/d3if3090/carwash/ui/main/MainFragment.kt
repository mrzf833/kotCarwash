package org.d3if3090.carwash.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.d3if3090.carwash.MainActivity
import org.d3if3090.carwash.R
import org.d3if3090.carwash.databinding.FragmentMainBinding
import org.d3if3090.carwash.db.CarwashDb
import org.d3if3090.carwash.db.HistoryEntity
import org.d3if3090.carwash.db.hasilCarwash
import org.d3if3090.carwash.model.DataTipeJasa
import org.d3if3090.carwash.model.HasilCarwash
import org.d3if3090.carwash.model.TipeJasa
import org.d3if3090.carwash.network.ApiStatus
import org.d3if3090.carwash.network.MainApi

class MainFragment: Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        val db = CarwashDb.getInstance(requireContext())
        val factory = MainViewModelFactory(db.historyDao)
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private var biaya: Int = 0

    private var idHistory: Long = 0

    private  var jasa: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getHasilCarwas().observe(requireActivity()){
            showResult(it)
        }

        binding.btnDetail.setOnClickListener {
            viewModel.mulaiNavigasiToDetail(idHistory)
        }

        viewModel.getNavigasiToDetail().observe(viewLifecycleOwner){
            if(it == null) return@observe
            findNavController().navigate(MainFragmentDirections
                .actionMainFragmentToDetailFragment(it))
            viewModel.selesaiNavigasiToDetail()
        }

        viewModel.getStatusTipeJasa().observe(viewLifecycleOwner) {
            updateProgressTipeJasa(it)
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
        viewModel.getDataTipeJasa().observe(viewLifecycleOwner){
            val dataTipeJasaList = viewModel.dataTipeJasaOnlyNama()
            val adapterTipeJasa = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, dataTipeJasaList)
            adapterTipeJasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterTipeJasa
            Log.d("tipe jasa", "ini: ${it}")
        }


        binding.btnSubmit.setOnClickListener{
            submit()
            viewModel.scheduleUpdater(requireActivity().application)
        }

        binding.btnReset.setOnClickListener{
            reset()
        }

        binding.btnBagikanMain.setOnClickListener { shareData() }

        spinner.selected {  }

        Glide.with(binding.imageView.context)
            .load(MainApi.getCarLogoUrl())
            .error(R.drawable.baseline_broken_image_24)
            .into(binding.imageView)

        return binding.root
    }

    private fun shareData(){
        var historyEntity: HistoryEntity? = null
        viewModel.dataLastHistory.observe(viewLifecycleOwner){
            historyEntity = it
        }
        if (historyEntity == null){
            Toast.makeText(this.context, "data tidak ada", Toast.LENGTH_LONG).show()
            return
        }
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
        // ini adalah untuk ngeset data carwash di main view model
        viewModel.setCarwash(namaKonsumen, namaMobil,noPol, jasa, bayarInp)
        Toast.makeText(this.context, R.string.submit_berhasil, Toast.LENGTH_LONG).show()

        // ini itu untuk di perpindahan ke detail fragment
        viewModel.dataLastHistory.observe(viewLifecycleOwner){
            idHistory = it.id
        }
    }

    private fun showResult(result: HasilCarwash?){
        if (result == null) {
            hideResult()
            return
        }
        binding.txtNamaSubmit.text = getString(R.string.nama_submit, result.nama)
        binding.txtJasaSubmit.text = getString(R.string.jasa_submit, result.jasa)
        binding.txtMobilSubmit.text = getString(R.string.mobil_submit, result.mobil)
        binding.txtBiayaSubmit.text = getString(R.string.biaya_submit, result.biaya)
        binding.txtKembalianSubmit.text = getString(R.string.kembalian_submit, result.kembalian.toString())

        tampilkanSubmit()
    }


    fun tampilkanSubmit(){
        binding.btnDetail.setVisibility(View.VISIBLE)
        binding.btnBagikanMain.setVisibility(View.VISIBLE)
        binding.txtNamaSubmit.setVisibility(View.VISIBLE)
        binding.txtJasaSubmit.setVisibility(View.VISIBLE)
        binding.txtMobilSubmit.setVisibility(View.VISIBLE)
        binding.txtBiayaSubmit.setVisibility(View.VISIBLE)
        binding.txtKembalianSubmit.setVisibility(View.VISIBLE)
    }

    fun hideResult(){
        binding.btnDetail.setVisibility(View.GONE)
        binding.btnBagikanMain.setVisibility(View.GONE)
        binding.txtNamaSubmit.setVisibility(View.GONE)
        binding.txtJasaSubmit.setVisibility(View.GONE)
        binding.txtMobilSubmit.setVisibility(View.GONE)
        binding.txtBiayaSubmit.setVisibility(View.GONE)
        binding.txtKembalianSubmit.setVisibility(View.GONE)
    }
    fun reset(){
        hideResult()

        viewModel.setNullCarwash()
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

    private fun updateProgressTipeJasa(status: ApiStatus) {
        binding.mainView.visibility = View.GONE
        binding.proggresView.visibility = View.VISIBLE
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.mainView.visibility = View.VISIBLE
                binding.proggresView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }

}