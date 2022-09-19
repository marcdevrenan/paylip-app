package br.edu.infnet.paylipapp

class Calculate {

    fun calculateINSS(etSalary: Double): Double {
        var inss = 0.0
        val etSalaryQuant = etSalary

        if (etSalaryQuant <= 1659.38){
            inss = etSalaryQuant*(0.08)
        }
        if ((etSalaryQuant> 1659.38) && (etSalaryQuant <= 2765.66)){
            inss = etSalaryQuant*(0.09)
        }
        if ((etSalaryQuant> 2765.66) && (etSalaryQuant <= 5531.31)){
            inss = etSalaryQuant*(0.11)
        }
        if (etSalaryQuant >= 5531.31){
            inss = 608.44
        }
        return inss;
    }

    fun calculateIRPF(etSalaryQuant: Double): Double {
        var irpf = 0.0

        if (etSalaryQuant <= 1903.98){
            irpf = etSalaryQuant*(0)
        }
        if ((etSalaryQuant> 1903.98) && (etSalaryQuant <= 2826.65)){
            irpf = etSalaryQuant*(0.075)
        }
        if ((etSalaryQuant> 2765.65) && (etSalaryQuant <= 3751.05)){
            irpf = etSalaryQuant*(0.15)
        }
        if ((etSalaryQuant> 3751.06) && (etSalaryQuant <= 4664.68)){
            irpf =etSalaryQuant*(0.225)
        }
        if ((etSalaryQuant> 4664.68)){
            irpf = etSalaryQuant*(0.275)
        }
        return irpf;
    }

    fun calculateLiquid(etSalaryQuant: Double,
                        inss: Double,
                        irpf: Double,
                        etAlimonyQuant: Double,
                        etDependentsQuant: Double,
                        etOthersQuant: Double): Double {

        val liquid = etSalaryQuant - inss - irpf - etAlimonyQuant- etDependentsQuant -etOthersQuant
        return liquid;
    }


}