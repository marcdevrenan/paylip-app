package br.edu.infnet.paylipapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.LineNumberReader
import java.io.OutputStreamWriter
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

                saveToFile(salary, dependents, alimony, others)
                startActivity(intent)

            } else {
                getString(R.string.insira_o_valor_do_sal_rio_bruto)
            }
        }
    }

    private fun saveToFile(salary: String, dependents: String, alimony: String, others: String) {
        val fos: FileOutputStream = this.openFileOutput("dados-usuario.txt", MODE_PRIVATE)
        val date = Date().toString()
        val texto = "Salary: $salary\n" +
                    "Dependents: $dependents\n" +
                    "alimony: $alimony\n" +
                    "Others: $others\n + " +
                    "Date: $date"
        fos.write(texto.toByteArray())
        fos.close()
    }
}

