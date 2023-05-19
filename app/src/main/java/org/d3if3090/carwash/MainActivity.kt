package org.d3if3090.carwash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

//    private lateinit var binding: ActivityMainBinding
//    private val viewModel: MainViewModel by lazy {
//        ViewModelProvider(this)[MainViewModel::class.java]
//    }
//
//    private var biaya: Int = 0
//
//    private  var jasa: String = ""
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val spinner: Spinner = binding.tipeJasaSpinner
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.tipe_jasa_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
//
//        binding.btnSubmit.setOnClickListener{
//            submit()
//        }
//
//        binding.btnReset.setOnClickListener{
//            reset()
//        }
//
//        spinner.selected {  }
//        viewModel.getHasilCarwas().observe(this){
//            showResult(it)
//        }
//    }
//
//
//    fun Spinner.selected(action: (position:Int) -> Unit) {
//        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                action(position)
//                val tipe_jasa = parent?.getItemAtPosition(position).toString()
//                jasa = tipe_jasa
//                val totalBiaya: String
//
//                val getTipeJasa = viewModel.getTipeJasa(tipe_jasa)
//                biaya = getTipeJasa.biaya
//                totalBiaya = getTipeJasa.totalBiaya
//                showTotalBiaya(totalBiaya)
//            }
//        }
//    }
//
//    private fun showTotalBiaya(totalBiaya: String){
//        binding.totalBiayaInp.setText(totalBiaya)
//    }
//
//    fun submit(){
//        val namaKonsumen = binding.namaKonsumenInp.text.toString()
//        if (TextUtils.isEmpty(namaKonsumen)) {
//            Toast.makeText(this, R.string.nama_invalid, Toast.LENGTH_LONG).show()
//            return
//        }
//
//        if (TextUtils.isEmpty(jasa)) {
//            Toast.makeText(this, R.string.jasa_invalid, Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val noPol = binding.noPolInp.text.toString()
//        if (TextUtils.isEmpty(noPol)) {
//            Toast.makeText(this, R.string.nopol_invalid, Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val bayarInp: Int
//        try {
//            val bayarCheck = binding.bayarInp.text.toString()
//            if (TextUtils.isEmpty(bayarCheck)) {
//                Toast.makeText(this, R.string.bayar_invalid, Toast.LENGTH_LONG).show()
//                return
//            }
//
//            bayarInp = bayarCheck.toInt()
//        }catch (ex: NumberFormatException){
//            Toast.makeText(this, R.string.bayar_invalid, Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val namaMobil = binding.namaMobilInp.text.toString()
//        if (TextUtils.isEmpty(namaMobil)) {
//            Toast.makeText(this, R.string.mobil_invalid, Toast.LENGTH_LONG).show()
//            return
//        }
//        val biayaInpText = binding.totalBiayaInp.text.toString()
//        if (TextUtils.isEmpty(biayaInpText)) {
//            Toast.makeText(this, R.string.biaya_invalid, Toast.LENGTH_LONG).show()
//            return
//        }
//        if(bayarInp - biaya < 0){
//            Toast.makeText(this, R.string.bayar_kurang, Toast.LENGTH_LONG).show()
//            return
//        }
//        viewModel.getCarwash(namaKonsumen, namaMobil, jasa, bayarInp)
//        Toast.makeText(this, R.string.submit_berhasil, Toast.LENGTH_LONG).show()
//    }
//
//    private fun showResult(result: HasilCarwash?){
//        if (result == null) return
//        binding.txtNamaSubmit.text = getString(R.string.nama_submit, result.nama)
//        binding.txtJasaSubmit.text = getString(R.string.jasa_submit, result.jasa)
//        binding.txtMobilSubmit.text = getString(R.string.mobil_submit, result.mobil)
//        binding.txtBiayaSubmit.text = getString(R.string.biaya_submit, result.biaya)
//        binding.txtKembalianSubmit.text = getString(R.string.kembalian_submit, result.kembalian.toString())
//
//        tampilkanSubmit()
//    }
//
//
//    fun tampilkanSubmit(){
//        binding.txtNamaSubmit.setVisibility(View.VISIBLE)
//        binding.txtJasaSubmit.setVisibility(View.VISIBLE)
//        binding.txtMobilSubmit.setVisibility(View.VISIBLE)
//        binding.txtBiayaSubmit.setVisibility(View.VISIBLE)
//        binding.txtKembalianSubmit.setVisibility(View.VISIBLE)
//    }
//
//    fun reset(){
//        binding.txtNamaSubmit.setVisibility(View.GONE)
//        binding.txtJasaSubmit.setVisibility(View.GONE)
//        binding.txtMobilSubmit.setVisibility(View.GONE)
//        binding.txtBiayaSubmit.setVisibility(View.GONE)
//        binding.txtKembalianSubmit.setVisibility(View.GONE)
//
//        binding.namaKonsumenInp.setText("")
//        binding.noPolInp.setText("")
//        binding.namaMobilInp.setText("")
//        binding.bayarInp.setText("")
//
//        Toast.makeText(this, R.string.reset_berhasil, Toast.LENGTH_LONG).show()
//    }
}