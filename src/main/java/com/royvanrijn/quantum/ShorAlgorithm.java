package com.royvanrijn.quantum;

import java.util.ArrayList;
import java.util.List;

public class ShorAlgorithm {
    public static void main(String[] args) {
        int N = 15; // Number to factor
        int a = 7;  // Co-prime number with N (e.g., a = 7)
        int numQubits = 4; // Number of qubits for the demonstration
        int numRuns = 10; // Number of runs to collect results

        List<Integer> measurements = new ArrayList<>();

        // Run the quantum part of the algorithm multiple times
        for (int i = 0; i < numRuns; i++) {
            QSystem qSystem = new QSystem(numQubits * 2);

            // Apply Hadamard gate to the first set of qubits
            for (int j = 0; j < numQubits; j++) {
                qSystem.applyGate(Gates.HADAMARD_GATE, j);
            }

            // Apply modular exponentiation
            qSystem.applyModularExponentiation(a, N, numQubits);

            // Apply Quantum Fourier Transform
            qSystem.applyGate(Gates.FOURIER_GATE(numQubits), 0, numQubits - 1);

            // Apply inverse Quantum Fourier Transform
            qSystem.applyInverseFourierGate(numQubits);

            // Measure the qubits and collect the result
            int result = qSystem.measure();
            measurements.add(result);
        }

        // Print the collected measurements
        System.out.println("Collected measurements: " + measurements);

        // Perform classical post-processing to determine the period
        int period = findPeriod(measurements, N, a, numQubits * 2);
        System.out.println("Estimated period: " + period);

        // Use the period to find the factors of N
        if (period > 0) {
            int factor1 = gcd((int) Math.pow(a, period / 2) - 1, N);
            int factor2 = gcd((int) Math.pow(a, period / 2) + 1, N);
            System.out.println("Factors of " + N + " are: " + factor1 + " and " + factor2);
        } else {
            System.out.println("Failed to find the period.");
        }
    }

    // Classical post-processing to find the period from the measurements
    private static int findPeriod(List<Integer> measurements, int N, int a, int totalQubits) {
        int period = 0;
        double q = Math.pow(2, totalQubits); // Calculate the denominator as 2^totalQubits

        for (int result : measurements) {
            double fraction = result / q;
            int denominator = findDenominator(fraction, N, a);
            if (denominator > 1) {
                period = denominator;
                break;
            }
        }

        return period;
    }

    private static int findDenominator(double fraction, int N, int a) {
        int maxDenominator = 1000; // Limit to prevent infinite loops
        for (int denominator = 1; denominator < maxDenominator; denominator++) {
            if (Math.abs(fraction * denominator - Math.round(fraction * denominator)) < 1e-6) {
                if (Math.pow(a, denominator) % N == 1) {
                    return denominator;
                }
            }
        }
        return 0;
    }

    // Euclidean algorithm to find the greatest common divisor (GCD)
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

}
