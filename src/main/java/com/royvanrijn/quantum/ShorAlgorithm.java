package com.royvanrijn.quantum;

public class ShorAlgorithm {

    public static void main(String[] args) {

        // This seems to be working as expected:

        // https://algassert.com/quirk#circuit=%7B%22cols%22:%5B%5B%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22%5D,%5B%22Z%22,%22%E2%80%A2%22%5D,%5B%22Bloch%22,%22Bloch%22%5D,%5B%22Chance10%22%5D,%5B%22QFT%E2%80%A010%22%5D,%5B%22Chance10%22%5D%5D%7D

        new QSystem(10)
                .applyGate(Gates.HADAMARD_GATE, 0)
                .applyGate(Gates.HADAMARD_GATE, 1)
                .applyGate(Gates.HADAMARD_GATE, 2)
                .applyGate(Gates.HADAMARD_GATE, 3)
                .applyGate(Gates.HADAMARD_GATE, 4)
                .applyGate(Gates.HADAMARD_GATE, 5)
                .applyGate(Gates.HADAMARD_GATE, 6)
                .applyGate(Gates.HADAMARD_GATE, 7)
                .applyGate(Gates.HADAMARD_GATE, 8)
                .applyGate(Gates.HADAMARD_GATE, 9)
                .applyGate(Gates.HADAMARD_GATE, 10)
                .applyGate(Gates.CZ_GATE, 0, 1)
                .applyGate(Gates.FOURIER_GATE(10), 0,1,2,3,4,5,6,7,8,9)
                .print()
                .measure();

        // Next step is implementing:
        // https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22],[%22Z%22,%22%E2%80%A2%22,%22%E2%80%A2%22],[%22Chance8%22],[%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22],[%22Swap%22,1,1,1,1,1,1,%22Swap%22],[1,%22Swap%22,1,1,1,1,%22Swap%22],[1,1,%22Swap%22,1,1,%22Swap%22],[1,1,1,%22Swap%22,%22Swap%22],[%22H%22],[%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,%22H%22],[%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,%22H%22],[%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%83%E2%82%82%22,%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%86%E2%82%84%22,%22Z^%E2%85%9F%E2%82%83%E2%82%82%22,%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%81%E2%82%82%E2%82%88%22,%22Z^%E2%85%9F%E2%82%86%E2%82%84%22,%22Z^%E2%85%9F%E2%82%83%E2%82%82%22,%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,1,1,1,%22H%22],[%22Chance8%22]]}

        // TODO: I'll need double control C-gates, implement a generic case for this
        // TODO: Z-gates with a partial 1/8, 1/4th etc.
    }
}
