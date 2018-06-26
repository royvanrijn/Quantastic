package com.royvanrijn.quantum;

public class QuantumComputerSimulator {

    public static void main(String[] args) {
        new QuantumComputerSimulator().run();
    }

    private void run() {

        new QSystem(3)
                .applyGate(Gates.HADAMARD_GATE, 0)
                .applyGate(Gates.HADAMARD_GATE, 1)
                .applyGate(Gates.CNOT_GATE, 0, 2)
                .applyGate(Gates.FOURIER_GATE(3), 0, 1, 2)
                .print()
                .measure()
                .print()
        ;
    }
}
