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


                    startActivity(intent)

            } else {
                getString(R.string.insira_o_valor_do_sal_rio_bruto)
            }
        }
    }

}

