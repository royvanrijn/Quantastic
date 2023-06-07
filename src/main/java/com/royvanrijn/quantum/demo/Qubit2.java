package com.royvanrijn.quantum.demo;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

/**
 *  This code shows the basis of a quantum computing, applying a quantum gate works like a matrix operation on the state space.
 */
@SuppressWarnings("all")
public class Qubit2 {

    public static void main(String[] args) {

        // Same system as before, now in a commons-math3 matrix:
        FieldMatrix<Complex> system = MatrixUtils.createFieldMatrix(
                new Complex[][] {{
                        new Complex(1.0), // α|0>
                        new Complex(0.0)  // β|1>
                }});

        // Pauli-X gate:
        // 0 1
        // 1 0
        FieldMatrix<Complex> pauliX = MatrixUtils.createFieldMatrix(new Complex[][] {
                {Complex.ZERO, Complex.ONE},
                {Complex.ONE, Complex.ZERO}
        });

        // Apply the gate to our system:
        system = system.multiply(pauliX);

        // Take a random measurement again:
        double measurement = Math.random();

        // Apply the measurement:
        int measuredState = 0;
        for(; measuredState < system.getRow(0).length; measuredState++) {
            measurement -= Math.pow(system.getRow(0)[measuredState].getReal(), 2);
            if(measurement < 0) {
                break;
            }
        }

        // Result:
        System.out.println("Resulting state: " + measuredState);

    }
}
