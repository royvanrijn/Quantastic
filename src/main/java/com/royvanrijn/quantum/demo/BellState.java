package com.royvanrijn.quantum.demo;

import java.util.Arrays;

import com.royvanrijn.quantum.Gates;
import com.royvanrijn.quantum.QSystem;

public class BellState {


    public static void main(String[] args) {

        // Bell state, entangle two qubits:
        int[] states = new int[4];

        for(int i = 0; i < 1000; i++) {
            int measured = new QSystem(2)
                    .applyGate(Gates.HADAMARD_GATE, 0)
                    .applyGate(Gates.CNOT_GATE, 0, 1)
                    .measure();

            states[measured]++;
        }

        System.out.println(Arrays.toString(states));
        // |00> : 50%
        // |01> :  0%
        // |10> :  0%
        // |11> : 50%

    }
}
