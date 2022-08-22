package br.edu.infnet.paylipapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private companion object{
        //PERMISSION request constant, assign any value
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etSalary = findViewById<EditText>(R.id.etSalary)
        val etDependents = findViewById<EditText>(R.id.etDependents)
        val etAlimony = findViewById<EditText>(R.id.etAlimony)
        val etOthers = findViewById<EditText>(R.id.etOthers)

        val btCalculate = findViewById<Button>(R.id.btNewSimulate)
        btCalculate.setOnClickListener {
            if (etSalary.toString().isNotBlank()) {
                val salary = etSalary.text.toString()
                val dependents = etDependents.text.toString()
                val alimony = etAlimony.text.toString()
                val others = etOthers.text.toString()

                val intent = Intent(this, ResultAcitivity::class.java)
                intent.putExtra("etSalary", salary)
                intent.putExtra("etDependents", dependents)
                intent.putExtra("etAlimony", alimony)
                intent.putExtra("etOthers", others)

                if (checkPermission()){
                    Log.d(TAG, "onCreate: Permission already granted, create folder")
                    saveToFile(salary, dependents, alimony, others)
                    startActivity(intent)
                }
                else{
                    Log.d(TAG, "onCreate: Permission was not granted, request")
                    requestPermission()
                }
            } else {
                getString(R.string.insira_o_valor_do_sal_rio_bruto)
            }
        }
    }


    private fun saveToFile(salary: String, dependents: String, alimony: String, others: String) {


              try {
                  val fos: FileOutputStream =
                      this.openFileOutput("dados-usuario.txt", MODE_PRIVATE)
                  val date = Date().toString()
                  var text = "Salary: $salary\n" +
                          "Dependents: $dependents\n" +
                          "alimony: $alimony\n" +
                          "Others: $others\n + " +
                          "Date: $date\n" +
                          "-------------------------------------------------\n"
                  val path = "/data/data/br.edu.infnet.paylipapp/files/dados-usuario.txt"
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
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            }
            catch (e: Exception){
                Log.e(TAG, "requestPermission: ", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }
    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(
    )){
        Log.d(TAG, "storageActivityResultLauncher: ")
        //here we will handle the result
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()){
                //Manage External Storage Permission is granted
                Log.d(TAG, "storageActivityResultLauncher: Manage External Storage Permission is granted")
                val etSalary = findViewById<EditText>(R.id.etSalary)
                val etDependents = findViewById<EditText>(R.id.etDependents)
                val etAlimony = findViewById<EditText>(R.id.etAlimony)
                val etOthers = findViewById<EditText>(R.id.etOthers)
                saveToFile(etSalary.text.toString(),etDependents.text.toString(),etAlimony.text.toString(),etOthers.text.toString())

            }
            else{
                //Manage External Storage Permission is denied....
                Log.d(TAG, "storageActivityResultLauncher: Manage External Storage Permission is denied....")
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
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()){
                //check each permission if granted or not
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read){
                    //External Storage Permission granted
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission granted")
                    val etSalary = findViewById<EditText>(R.id.etSalary)
                    val etDependents = findViewById<EditText>(R.id.etDependents)
                    val etAlimony = findViewById<EditText>(R.id.etAlimony)
                    val etOthers = findViewById<EditText>(R.id.etOthers)
                    saveToFile(etSalary.text.toString(),etDependents.text.toString(),etAlimony.text.toString(),etOthers.text.toString())

                }
                else{
                    //External Storage Permission denied...
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission denied...")
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





}

