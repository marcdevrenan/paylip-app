package br.edu.infnet.paylipapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.io.*
import java.util.*

class ResultAcitivity : AppCompatActivity() {
    //Anuncio somente ao clicar no botão salvar
    private var _interstitialAd: InterstitialAd? = null
    private var _bannerAd: AdView? = null

    private companion object{
        //PERMISSION request constant, assign any value
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_acitivity)

        this.liquidSalary()

        val btNewSimulate : TextView = findViewById<Button>(R.id.btNewSimulate)
        btNewSimulate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btDelete : TextView = findViewById<Button>(R.id.btDelete)
        btDelete.setOnClickListener {
            deletar()
        }

        val btSave : TextView = findViewById<Button>(R.id.btSave)
        btSave.setOnClickListener {

            val etSalary = intent.getStringExtra("etSalary")
            val etDependents = intent.getStringExtra("etDependents")
            val etAlimony = intent.getStringExtra("etAlimony")
            val etOthers = intent.getStringExtra("etOthers")

            val salary = etSalary.toString()
            val dependents = etDependents.toString()
            val alimony = etAlimony.toString()
            val others = etOthers.toString()

            val intent = Intent(this, ResultAcitivity::class.java)
            intent.putExtra("etSalary", salary)
            intent.putExtra("etDependents", dependents)
            intent.putExtra("etAlimony", alimony)
            intent.putExtra("etOthers", others)

            if (checkPermission()){
                Log.d(TAG, "onCreate: Permission already granted, create folder")
                saveToFile(salary, dependents, alimony, others)
            }
            else{
                Log.d(TAG, "onCreate: Permission was not granted, request")
                requestPermission()
            }

            //inicia o banner e suas configurações para mostrar em tela
            MobileAds.initialize(this)
            loadInterstitial()
            loadBannerAd()

        }

    }

    //carrega o tipo de anuncio
    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                _interstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                _interstitialAd = interstitialAd
            }
        })
    }

    //carrega a propaganda
    private fun loadBannerAd() {
        _bannerAd = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        _bannerAd?.loadAd(adRequest)
    }

    //mostra o tipo de anuncio
    fun showInterstitial(view: View) {
        _interstitialAd?.show(this)
    }

    private fun liquidSalary(){

        val tvResultSalaryBrut : TextView = findViewById (R.id.tvResultSalaryBrut)
        val tvResultInss : TextView  = findViewById(R.id.tvResultInss)
        val tvResultIrpf : TextView = findViewById(R.id.tvResultIrpf)
        val tvResultAlimony : TextView = findViewById(R.id.tvResultAlimony)
        val tvResultHealthCare : TextView = findViewById(R.id.tvResultHealthCare)
        val tvResultOthers : TextView = findViewById(R.id.tvResultOthers)
        val tvLiquid : TextView = findViewById (R.id.tvLiquid)

        val etSalary = intent.getStringExtra("etSalary")
        val etDependents = intent.getStringExtra("etDependents")
        val etAlimony = intent.getStringExtra("etAlimony")
        val etOthers = intent.getStringExtra("etOthers")
        var inss = 0.0
        var irpf = 0.0

        var calculate = Calculate()

        //1o calculate INSS
        inss = calculate.calculateINSS(etSalary.toString().toDouble())

        tvResultInss.text = inss.toString()

        //2o alimony
        val etAlimonyQuant = etAlimony.toString().toDouble()
        tvResultAlimony.text = etAlimony

        val etOthersQuant = etOthers.toString().toDouble()
        tvResultOthers.text = etOthers

        //3o irpf
        irpf = calculate.calculateIRPF(etSalary.toString().toDouble())

        tvResultIrpf.text = irpf.toString()

        //4o saúde
        val etDependentsQuant = etDependents.toString().toDouble() * 189.59
        tvResultHealthCare.text = etDependentsQuant.toString()

        //5o liquid
        var liquid = calculate.calculateLiquid(etSalary.toString().toDouble(),
            inss,
            irpf,
            etAlimonyQuant,
            etDependentsQuant,
            etOthersQuant)
        //tvLiquid.text = liquid.toString()
        tvLiquid.text = "%.2f".format(liquid)

    }



    fun deletar(){
        val file = File("/data/data/br.edu.infnet.paylipapp/files/dadosUsuario.txt")

        val result = file.delete()
        if (result) {
            Toast.makeText(this, "Deletado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }



    private fun saveToFile(salary: String, dependents: String, alimony: String, others: String) {
        try {
            val fos: FileOutputStream =
                this.openFileOutput("dadosUsuario.txt", MODE_PRIVATE)
            val date = Date().toString()
            var text = "Salary: $salary\n" +
                    "Dependents: $dependents\n" +
                    "alimony: $alimony\n" +
                    "Others: $others\n + " +
                    "Date: $date\n" +
                    "-------------------------------------------------\n"

            val path = "/data/data/br.edu.infnet.paylipapp/files/dadosUsuario.txt"
            val data = text
            try {
                FileWriter(path, true).use {
                    it.write(data)
                }
            }catch (e: IOException) {
                e.printStackTrace()
            }

            Toast.makeText(this, "Salvo", Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            try {
                Log.d(ResultAcitivity.TAG, "requestPermission: try")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            }
            catch (e: Exception){
                Log.e(ResultAcitivity.TAG, "requestPermission: ", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                ResultAcitivity.STORAGE_PERMISSION_CODE
            )
        }
    }
    private val storageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(
    )){
        Log.d(ResultAcitivity.TAG, "storageActivityResultLauncher: ")
        //here we will handle the result
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()){
                //Manage External Storage Permission is granted
                Log.d(ResultAcitivity.TAG, "storageActivityResultLauncher: Manage External Storage Permission is granted")
                val etSalary = findViewById<EditText>(R.id.etSalary)
                val etDependents = findViewById<EditText>(R.id.etDependents)
                val etAlimony = findViewById<EditText>(R.id.etAlimony)
                val etOthers = findViewById<EditText>(R.id.etOthers)
                saveToFile(etSalary.text.toString(),etDependents.text.toString(),etAlimony.text.toString(),etOthers.text.toString())

            }
            else{
                //Manage External Storage Permission is denied....
                Log.d(ResultAcitivity.TAG, "storageActivityResultLauncher: Manage External Storage Permission is denied....")
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
        else{
        }
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }
        else{
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ResultAcitivity.STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()){
                //check each permission if granted or not
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read){
                    //External Storage Permission granted
                    Log.d(ResultAcitivity.TAG, "onRequestPermissionsResult: External Storage Permission granted")
                    val etSalary = findViewById<EditText>(R.id.etSalary)
                    val etDependents = findViewById<EditText>(R.id.etDependents)
                    val etAlimony = findViewById<EditText>(R.id.etAlimony)
                    val etOthers = findViewById<EditText>(R.id.etOthers)
                    saveToFile(etSalary.text.toString(),etDependents.text.toString(),etAlimony.text.toString(),etOthers.text.toString())

                }
                else{
                    //External Storage Permission denied...
                    Log.d(ResultAcitivity.TAG, "onRequestPermissionsResult: External Storage Permission denied...")
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}