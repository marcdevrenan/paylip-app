package br.edu.infnet.paylipapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultAcitivity : AppCompatActivity() {

    lateinit var tvResultSalaryBrut : TextView
    lateinit var tvResultInss : TextView
    lateinit var tvResultIrpf : TextView
    lateinit var tvResultAlimony : TextView
    lateinit var tvResultOthers : TextView
    lateinit var tvLiquid : TextView
    lateinit var btNewSimulate : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_acitivity)

        tvResultSalaryBrut = findViewById<TextView>(R.id.tvResultSalaryBrut)
        tvResultInss = findViewById<TextView>(R.id.tvResultInss)
        tvResultIrpf = findViewById<TextView>(R.id.tvResultIrpf)
        tvResultAlimony = findViewById<TextView>(R.id.tvResultAlimony)
        tvResultOthers = findViewById<TextView>(R.id.tvResultOthers)
        tvLiquid = findViewById<TextView>(R.id.tvLiquid)
        btNewSimulate = findViewById<Button>(R.id.btNewSimulate)

        liquidSalary()

        btNewSimulate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun liquidSalary(){
        val etSalary = intent.getStringExtra("etSalary")
        val etDependents = intent.getStringExtra("etDependents")
        val etAlimony = intent.getStringExtra("etAlimony")
        val etOthers = intent.getStringExtra("etOthers")
        var inss = 0.0
        var irpf = 0.0

        //1o calculate INSS
        var etSalaryQuant = etSalary.toString().toDouble()
        tvResultSalaryBrut.text= etSalary

        if (etSalaryQuant <= 1659.38){
            inss = etSalaryQuant - ((etSalaryQuant*8)/100)
        }
        if ((etSalaryQuant> 1659.38) && (etSalaryQuant <= 2765.66)){
            inss = etSalaryQuant - ((etSalaryQuant*9)/100)
        }
        if ((etSalaryQuant> 2765.66) && (etSalaryQuant <= 5531.31)){
            inss = etSalaryQuant - ((etSalaryQuant*11)/100)
        }
        if (etSalaryQuant >= 5531.31){
            inss = 608.44
        }
        tvResultInss.text = inss.toString()

        //2o alimony
        var etAlimonyQuant = etAlimony.toString().toDouble()
        tvResultAlimony.text = etAlimony

        var etOthersQuant = etOthers.toString().toDouble()
        tvResultOthers.text = etOthers

        //3o irpf
        if (etSalaryQuant <= 1903.98){
            irpf = etSalaryQuant - ((etSalaryQuant*0)/100)
        }
        if ((etSalaryQuant> 1903.98) && (etSalaryQuant <= 2826.65)){
            irpf = etSalaryQuant - ((etSalaryQuant*7.5)/100)
        }
        if ((etSalaryQuant> 2765.65) && (etSalaryQuant <= 3751.05)){
            irpf = etSalaryQuant - ((etSalaryQuant*15)/100)
        }
        if ((etSalaryQuant> 3751.06) && (etSalaryQuant <= 4664.68)){
            irpf = etSalaryQuant - ((etSalaryQuant*22.5)/100)
        }
        if ((etSalaryQuant> 4664.68) && (etSalaryQuant <= 4664.68)){
            irpf = etSalaryQuant - ((etSalaryQuant*27.5)/100)
        }
        tvResultIrpf.text = irpf.toString()

        //4o saúde
        var etDependentsQuant = (etDependents.toString().toDouble() * 189.59)

        //5o liquid
        var liquid = etSalaryQuant-inss-irpf-etAlimonyQuant-etDependentsQuant-etOthersQuant
        tvLiquid.text = liquid.toString()

    }


}