package com.royvanrijn.quantum.demo;

import org.apache.commons.math3.complex.Complex;

/**
 *  This code shows the basis of a quantum system, a qubit as a complex number with probabilities of |0> and |1>
 */
@SuppressWarnings("all")
public class Qubit1 {


    public static void main(String[] args) {

        // Create a single qubit:
        Complex[] system = new Complex[] {
                new Complex(1.0), // α|0>
                new Complex(0.0)  // β|1>
        };

        // Take a random measurement:
        double measurement = Math.random();

        // Calculate where this measurement fits in the possibilities:
        int measuredState = 0;
        for(; measuredState < system.length; measuredState++) {
            measurement -= Math.pow(system[measuredState].getReal(), 2);
            if(measurement < 0) {
                break;
            }
        }

        // Result:
        System.out.println("Resulting state: " + measuredState);
    }
}
