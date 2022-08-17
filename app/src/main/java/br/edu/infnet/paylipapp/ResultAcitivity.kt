package br.edu.infnet.paylipapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultAcitivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_acitivity)


        val tvResultSalaryBrut = findViewById<TextView>(R.id.tvResultSalaryBrut)
        val tvResultInss = findViewById<TextView>(R.id.tvResultInss)
        val tvResultIrpf = findViewById<TextView>(R.id.tvResultIrpf)
        val tvResultAlimony = findViewById<TextView>(R.id.tvResultAlimony)
        val tvResultOthers = findViewById<TextView>(R.id.tvResultOthers)
        val btNewSimulate = findViewById<Button>(R.id.btNewSimulate)

        btNewSimulate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }


    }
}