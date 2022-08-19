package br.edu.infnet.paylipapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btCalculate = findViewById<Button>(R.id.btNewSimulate)
        val etSalary = findViewById<EditText>(R.id.etSalary).toString()
        val etDependents = findViewById<EditText>(R.id.etDependents).toString()
        val etAlimony = findViewById<EditText>(R.id.etAlimony).toString()
        val etOthers = findViewById<EditText>(R.id.etOthers).toString()



        btCalculate.setOnClickListener {
            if(etSalary.isNotBlank()) {
                val intent = Intent(this, ResultAcitivity::class.java)
                intent.putExtra("etSalary",etSalary)
                intent.putExtra("etDependents",etDependents)
                intent.putExtra("etAlimony",etAlimony)
                intent.putExtra("etOthers",etOthers)

                startActivity(intent)

            }else{
                getString(R.string.insira_o_valor_do_sal_rio_bruto)
            }

        }


    }


}