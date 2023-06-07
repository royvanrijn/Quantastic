package com.royvanrijn.quantum.demo;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

/**
 *  Same code as Qubit3, but with Schrodingers cat, it's mandatory.
 */
@SuppressWarnings("all")
public class SchrodingersCat {

    private static final String STATE_0 = "                                 _\n" +
            "                                | \\\n" +
            "                                | |\n" +
            "                                | |\n" +
            "           |\\                   | |\n" +
            "          /, ~\\                / /\n" +
            "         X     `-.....-------./ /\n" +
            "          ~-. ~  ~              |\n" +
            "             \\             /    |\n" +
            "              \\  /_     ___\\   /\n" +
            "              | /\\ ~~~~~   \\ |\n" +
            "              | | \\        || |\n" +
            "              | |\\ \\       || )\n" +
            "             (_/ (_/      ((_/";


    private static final String STATE_1 = "\n   |\\      _,,,---,,_\n" +
            "   /,`.-'`'    -.  ;-;;,_\n" +
            "  |,4-  ) )-,_..;\\ (  `'-'\n" +
            " '---''(_/--'  `-'\\_)  ";


    public static void main(String[] args) {

        // Same system as before, now in a commons-math3 matrix:
        FieldMatrix<Complex> system = MatrixUtils.createFieldMatrix(
                new Complex[][] {{
                        new Complex(1.0), // α|0>
                        new Complex(0.0)  // β|1>
                }});

        // Hadamard gate:
        FieldMatrix<Complex> hadamardGate = MatrixUtils.createFieldMatrix(new Complex[][] {
                {new Complex(1/Math.sqrt(2)),new Complex(1/Math.sqrt(2))},
                {new Complex(1/Math.sqrt(2)),new Complex(-1/Math.sqrt(2))}
        });

        // Apply the gate to our system:
        system = system.multiply(hadamardGate);

        boolean catAlive = measure(system) == 0;

        // Result:
        System.out.println("Schrödinger's cat experiment");
        System.out.println("Resulting state: " + (catAlive? "Felix survived!" : "Awww... he didn't make it"));
        System.out.println(catAlive? STATE_0 : STATE_1);

    }

    private static int measure(final FieldMatrix<Complex> system) {
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
        return measuredState;
    }
}
