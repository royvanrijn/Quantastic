package com.royvanrijn.quantum;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldMatrix;

public class QuantumComputerSimulator {

    public static void main(String[] args) {
        new QuantumComputerSimulator().run();
    }

    private void run() {

        new QSystem(3)
//                .applyGate(Gates.HADAMARD_GATE, 0)
//                .applyGate(Gates.HADAMARD_GATE, 2)
                .applyMultipleGate(Gates.HADAMARD_GATE, 0,2)
//                .applyGate(Gates.CNOT_GATE, 1,0)
//                .applyGate(Gates.SWAP_GATE, 0,1)
                .print()
                .measure();

        FieldMatrix<Complex> masterGate = Gates.createControlledGate(Gates.PAULI_X_GATE, new int[]{1}, new int[]{0});

        // Now create the master gate by taking the Kronecker product
        QSystem.prettyPrintMatrix(masterGate);


    }
}
