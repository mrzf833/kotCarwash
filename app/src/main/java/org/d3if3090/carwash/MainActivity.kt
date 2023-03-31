package org.d3if3090.carwash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import org.d3if3090.carwash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var biaya: Int = 0

    private  var jasa: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = binding.tipeJasaSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
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
    }

    fun Spinner.selected(action: (position:Int) -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action(position)
                val tipe_jasa = parent?.getItemAtPosition(position).toString()
                jasa = tipe_jasa
                val totalBiaya: String
                when(tipe_jasa){
                    "Cuci Mobil Manual" -> {
                        biaya = 30000
                        totalBiaya = "30.000"
                    }
                    "Cuci Mobil Waterless" -> {
                        biaya = 50000
                        totalBiaya = "50.000"
                    }
                    "Cuci Mobil Touchless" -> {
                        biaya = 50000
                        totalBiaya = "50.000"
                    }
                    "Cuci Mobil Robotic" -> {
                        biaya = 80000
                        totalBiaya = "80.000"
                    }
                    "Cuci Mobil Hidrolik" -> {
                        biaya = 30000
                        totalBiaya = "30.000"
                    }
                    else -> {
                        biaya = 0
                        totalBiaya = ""
                    }
                }
                binding.totalBiayaInp.setText(totalBiaya)
            }
        }
    }

    fun submit(){
        val namaKonsumen = binding.namaKonsumenInp.text.toString()
        if (TextUtils.isEmpty(namaKonsumen)) {
            Toast.makeText(this, R.string.nama_invalid, Toast.LENGTH_LONG).show()
            return
        }
        binding.txtNamaSubmit.text = getString(R.string.nama_submit, namaKonsumen)

        if (TextUtils.isEmpty(jasa)) {
            Toast.makeText(this, R.string.jasa_invalid, Toast.LENGTH_LONG).show()
            return
        }
        binding.txtJasaSubmit.text = getString(R.string.jasa_submit, jasa)

        val noPol = binding.noPolInp.text.toString()
        if (TextUtils.isEmpty(noPol)) {
            Toast.makeText(this, R.string.nopol_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val bayarInp: Int
        try {
            val bayarCheck = binding.bayarInp.text.toString()
            if (TextUtils.isEmpty(bayarCheck)) {
                Toast.makeText(this, R.string.bayar_invalid, Toast.LENGTH_LONG).show()
                return
            }

            bayarInp = bayarCheck.toInt()
        }catch (ex: NumberFormatException){
            Toast.makeText(this, R.string.bayar_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val namaMobil = binding.namaMobilInp.text.toString()
        if (TextUtils.isEmpty(namaMobil)) {
            Toast.makeText(this, R.string.mobil_invalid, Toast.LENGTH_LONG).show()
            return
        }
        binding.txtMobilSubmit.text = getString(R.string.mobil_submit, namaMobil)

        val biayaInpText = binding.totalBiayaInp.text.toString()
        if (TextUtils.isEmpty(biayaInpText)) {
            Toast.makeText(this, R.string.biaya_invalid, Toast.LENGTH_LONG).show()
            return
        }
        binding.txtBiayaSubmit.text = getString(R.string.biaya_submit, biayaInpText)

        if(bayarInp - biaya < 0){
            Toast.makeText(this, R.string.bayar_kurang, Toast.LENGTH_LONG).show()
            return
        }
        binding.txtKembalianSubmit.text = getString(R.string.kembalian_submit,(bayarInp - biaya).toString())
        tampilkanSubmit()

        Toast.makeText(this, R.string.submit_berhasil, Toast.LENGTH_LONG).show()
    }

    fun tampilkanSubmit(){
        binding.txtNamaSubmit.setVisibility(View.VISIBLE)
        binding.txtJasaSubmit.setVisibility(View.VISIBLE)
        binding.txtMobilSubmit.setVisibility(View.VISIBLE)
        binding.txtBiayaSubmit.setVisibility(View.VISIBLE)
        binding.txtKembalianSubmit.setVisibility(View.VISIBLE)
    }

    fun reset(){
        binding.txtNamaSubmit.setVisibility(View.GONE)
        binding.txtJasaSubmit.setVisibility(View.GONE)
        binding.txtMobilSubmit.setVisibility(View.GONE)
        binding.txtBiayaSubmit.setVisibility(View.GONE)
        binding.txtKembalianSubmit.setVisibility(View.GONE)

        binding.namaKonsumenInp.setText("")
        binding.noPolInp.setText("")
        binding.namaMobilInp.setText("")
        binding.bayarInp.setText("")

        Toast.makeText(this, R.string.reset_berhasil, Toast.LENGTH_LONG).show()
    }
}