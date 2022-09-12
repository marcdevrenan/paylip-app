package br.edu.infnet.paylipapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun calculateINSSMustReturnTheINSSValidValue() {
        //Arrange
        var etSalary = 100.00
        val calculate = Calculate()

        //Act
        var inss = calculate.calculateINSS(etSalary)

        //Assert
        assertTrue(inss == 8.0)
    }

    @Test
    fun calculateIRPFMustReturnTheIRPFValidValue() {
        //Arrange
        var etSalaryQuant = 2000.00
        var etSalaryQuant1 = 100.00
        val calculate = Calculate()

        //Act
        var irpf = calculate.calculateIRPF(etSalaryQuant)
        var irpf1 = calculate.calculateIRPF(etSalaryQuant1)

        //Assert
        assertTrue(irpf == 150.0)
        assertTrue(irpf1 == 0.0)
    }

    @Test
    fun calculateLiquidMustReturnTheLiquidValidValue() {
        //Arrange
        var etSalaryQuant = 187.00
        var inss = 788.00
        var irpf = 786.98
        var etAlimonyQuant = 987.00
        var etDependentsQuant = 199.00
        var etOthersQuant = 100.00

        val calculate = Calculate()

        //Act
        var liquid = calculate.calculateLiquid(etSalaryQuant,
                                                inss,
                                                irpf,
                                                etAlimonyQuant,
                                                etDependentsQuant,
                                                etOthersQuant)

        //Assert
        assertTrue(liquid == -2673.98)
    }
}