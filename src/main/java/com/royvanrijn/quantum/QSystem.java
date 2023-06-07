package com.royvanrijn.quantum;

import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

public class QSystem {

    // A very small number to clean up results
    private static final double ROUNDING = 0.0000000000001;

    private final int qubits;
    private final int systemSize;
    private FieldMatrix<Complex> system;

    public QSystem(final int qubitCount) {

        this.qubits = qubitCount;
        this.systemSize = (int) Math.pow(2, qubitCount);

        // Initialize the system with all zeros (000...n) = 1.0:
        initializeSystem(0);
    }

    /**
     * Put the entire quantum circuit to one fixed outcome.
     * @param setValue
     */
    private void initializeSystem(int setValue) {
        Complex[][] states = new Complex[1][systemSize];
        // Set initial probabilities to ZERO:
        Arrays.fill(states[0], Complex.ZERO);
        // Set initial probability of a single outcome:
        states[0][setValue] = Complex.ONE;

        this.system = MatrixUtils.createFieldMatrix(states);
    }

    /**
     * Apply a measurement to the quantum circuit.
     *
     * This puts the entire system into the measured state.
     * @return
     */
    public int measure() {

        double[] normalized = getNormalized();

        // Take a measurement over the normalized values:
        double measurement = Math.random();

        System.out.println("Measurement:");
        System.out.println("\tValue: " + measurement);

        int measuredState = 0;
        for(; measuredState < normalized.length; measuredState++) {
            measurement -= normalized[measuredState];
            if(measurement < 0) {
                break;
            }
        }

        System.out.println("\tState: " + toBinaryString(measuredState));

        // Put the entire system in a measured state:
        initializeSystem(measuredState);

        System.out.println("System collapsed into measured state.");
        return measuredState;
    }

    /**
     * Method for getting the normalized real values.
     * @return
     */
    private double[] getNormalized() {
        // Get all the probabilities of the system and square them:
        double[] values = Arrays.stream(system.getRow(0))
                        .mapToDouble(c -> Math.pow(c.getReal(), 2) + Math.pow(c.getImaginary(), 2))
                        .toArray();

        // Get the total sum:
        double valuesTotal = Arrays.stream(values).sum();
        // Normalize the values:
        return Arrays.stream(values).map(d -> d / valuesTotal).toArray();
    }

    /**
     * Apply a gate to the given wires.
     *
     * @param gate
     * @param wires
     * @return
     */
    public QSystem applyGate(final FieldMatrix<Complex> gate, final int... wires) {

        final Complex[][] gateData = gate.getData();

        System.out.println("Gate:");
        // TODO We don't need to apply each gate separately, most quantum systems/simulators have an option to apply multiple gates at different steps.
        // Should be possible to implement.

        FieldMatrix<Complex> masterGate = createEmptyMatrix();

        int[] sortedWires = Arrays.copyOf(wires, wires.length);
        Arrays.sort(sortedWires);

        for(int row = 0; row < systemSize; row++)  {
            for(int col = 0; col < systemSize; col++) {

                // Weird bit of code based on:
                // https://github.com/RoboNeo9/Java-Quantum-Computer-Simulator/blob/master/Core/MasterGate.java

                // First we look up which qubits are NOT part of the wires of this gate:
                int rowQubitsNotWire = 0;
                int colQubitsNotWire = 0;

                //TODO optimize this:
                for(int i = 0; i < qubits; i++) {
                    // If this is NOT part of the wire, add these bits to our bitstring:
                    if(Arrays.binarySearch(sortedWires, i) < 0) {
                        // Shift everything and add the current row and col bit:
                        rowQubitsNotWire = (rowQubitsNotWire << 1) | ((row >> i) & 1);
                        colQubitsNotWire = (colQubitsNotWire << 1) | ((col >> i) & 1);
                    }
                }

                if(rowQubitsNotWire == colQubitsNotWire) {

                    int rowQubitsWire = 0;
                    int colQubitsWire = 0;

                    //TODO optimize this:
                    // Beware, the order of wires here is important (in relation to the gate), reverse traversal matters
                    for(int i = wires.length-1; i >= 0; i--) {
                        int wire = wires[i];
                        // Shift everything and add the current row and col bit:
                        rowQubitsWire = (rowQubitsWire << 1) | ((row >> wire) & 1);
                        colQubitsWire = (colQubitsWire << 1) | ((col >> wire) & 1);
                    }

                    Complex data = gateData[rowQubitsWire][colQubitsWire];
                    masterGate.setEntry(row, col, clean(data));
                }
            }
        }

        // Print the matrix:
//        prettyPrintMatrix(masterGate);

        System.out.println(masterGate.getRowDimension()+" x "+masterGate.getColumnDimension());
        // TODO: If the gate we're applying only affects some qubits, like a single wire, do we need this large matrix?
        // Apply the gate matrix to the system:
        system = system.multiply(masterGate);

        prettyPrintMatrix(system);

        return this;
    }

    /**
     * Small method to clean up the gate
     * (for example the Fourier gate often has rounding errors, this makes small numbers zero again for readability)
     * @param data
     * @return
     */
    private Complex clean(Complex data) {
        // Clean up very small numbers to get a 'clean' gate
        if(Math.abs(data.getReal()) < ROUNDING) {
            data = data.subtract(data.getReal());
        }
        if(Math.abs(data.getImaginary()) < ROUNDING) {
            data = data.subtract(new Complex(0, data.getImaginary()));
        }
        return data;
    }

    private FieldMatrix<Complex> createEmptyMatrix() {
        Complex[][] initialMatrix = new Complex[systemSize][systemSize];
        for(int i = 0; i < systemSize; i++) Arrays.fill(initialMatrix[i], Complex.ZERO);
        return MatrixUtils.createFieldMatrix(initialMatrix);
    }

    private void prettyPrintMatrix(final FieldMatrix<Complex> masterGate) {
        System.out.println("Applying gate:");
        for(int i = 0; i < masterGate.getRowDimension(); i++) {
            System.out.print("\t");
            for(int z = 0; z < masterGate.getColumnDimension(); z++) {
                System.out.print(masterGate.getRow(i)[z]);
            }
            System.out.println();
        }
    }

    public QSystem print() {
        System.out.println(this);
        return this;
    }


    @Override
    public String toString() {
        // Pretty print the state of the system:
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Current state of system:")
                .append(System.lineSeparator());
        for(int ptr = 0; ptr < systemSize; ptr++) {
            stringBuilder
                    .append("\t")
                    .append(toBinaryString(ptr))
                    .append(": ")
                    .append(clean(system.getEntry(0, ptr)));
            if(ptr < systemSize - 1) {
                stringBuilder.append(System.lineSeparator());
            }
        }

        stringBuilder
                .append(System.lineSeparator())
                .append("Chances for qubits measured ON:")
                .append(System.lineSeparator());

        double[] chances = new double[qubits];
        double[] normalized = getNormalized();
        for(int i = 0; i < systemSize; i++) {
            for(int qubit = 0; qubit < qubits; qubit++) {
                if(((1 << qubit) & i) > 0) {
                    chances[qubit] += normalized[i];
                }
            }
        }
        for(int qubit = 0; qubit < qubits; qubit++) {
            stringBuilder
                    .append("\t")
                    .append(qubit)
                    .append(": ")
                    .append(String.format("%.2f", chances[qubit]))
                    .append("%")
                    .append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    private String toBinaryString(final int measuredState) {
        return String.format("%0"+qubits+"d", Integer.parseInt(Integer.toBinaryString(measuredState)));
    }
}
