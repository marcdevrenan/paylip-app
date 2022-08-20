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

        this.liquidSalary()

        val btNewSimulate : TextView = findViewById<Button>(R.id.btNewSimulate)
        btNewSimulate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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

        //1o calculate INSS
        val etSalaryQuant = etSalary.toString().toDouble()
        tvResultSalaryBrut.text= etSalary

        if (etSalaryQuant <= 1659.38){
            inss = etSalaryQuant*(8/100)
        }
        if ((etSalaryQuant> 1659.38) && (etSalaryQuant <= 2765.66)){
            inss = etSalaryQuant*(9/100)
        }
        if ((etSalaryQuant> 2765.66) && (etSalaryQuant <= 5531.31)){
            inss = etSalaryQuant*(11/100)
        }
        if (etSalaryQuant >= 5531.31){
            inss = 608.44
        }
        tvResultInss.text = inss.toString()

        //2o alimony
        val etAlimonyQuant = etAlimony.toString().toDouble()
        tvResultAlimony.text = etAlimony

        val etOthersQuant = etOthers.toString().toDouble()
        tvResultOthers.text = etOthers

        //3o irpf
        if (etSalaryQuant <= 1903.98){
            irpf = etSalaryQuant*(0/100)
        }
        if ((etSalaryQuant> 1903.98) && (etSalaryQuant <= 2826.65)){
            irpf = etSalaryQuant*(7.5/100)
        }
        if ((etSalaryQuant> 2765.65) && (etSalaryQuant <= 3751.05)){
            irpf = etSalaryQuant*(15/100)
        }
        if ((etSalaryQuant> 3751.06) && (etSalaryQuant <= 4664.68)){
            irpf =etSalaryQuant*(22.5/100)
        }
        if ((etSalaryQuant> 4664.68) && (etSalaryQuant <= 4664.68)){
            irpf = etSalaryQuant*(27.5/100)
        }
        tvResultIrpf.text = irpf.toString()

        //4o saúde
        val etDependentsQuant = etDependents.toString().toDouble() * 189.59
        tvResultHealthCare.text = etDependentsQuant.toString()

        //5o liquid

        val liquid = etSalaryQuant - inss - irpf - etAlimonyQuant- etDependentsQuant -etOthersQuant
        tvLiquid.text = liquid.toString()

    }


}