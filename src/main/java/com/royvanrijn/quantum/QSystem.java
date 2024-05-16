package com.royvanrijn.quantum;

import static com.royvanrijn.quantum.Gates.FOURIER_GATE;

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
     * In a simulator we can do a sneaky measure... without affecting the system
     * @return
     */
    public void sneakyMeasure(int amount) {

        double[] normalized = getNormalized();

        int[] bitsTrue = new int[qubits];

        for(int i = 0; i < amount; i++) {
            // Take a measurement over the normalized values:
            double measurement = Math.random();

            int measuredState = 0;
            for(; measuredState < normalized.length; measuredState++) {
                measurement -= normalized[measuredState];
                if(measurement < 0) {
                    break;
                }
            }

            for(int x = 0; x < qubits; x++) {
                if((measuredState&(1<<x))!=0) {
                    bitsTrue[x]++;
                }
            }
            System.out.println("\tState: " + toBinaryString(measuredState) + " " + measuredState + " "+ (measuredState*1.0)/(Math.pow(2,qubits)));
        }

        System.out.println(Arrays.toString(bitsTrue));



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


    public QSystem applyMultipleGate(final FieldMatrix<Complex> gate, final int... wires) {
        final int matrixQubits = (int) Math.sqrt(gate.getRowDimension());
        int kroneckerSize = wires.length/matrixQubits;
        FieldMatrix<Complex> matrix = gate;
        for(int times = 1; times < kroneckerSize; times++) {
            matrix = Gates.kroneckerProduct(matrix, gate);
        }
        return applyGate(matrix, wires);
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

        // TODO We don't need to apply each gate separately, most quantum systems/simulators have an option to apply multiple gates at different steps.
        // Should be possible to implement.

        FieldMatrix<Complex> masterGate = createEmptyMatrix(systemSize, systemSize);

        int[] sortedWires = Arrays.copyOf(wires, wires.length);
        Arrays.sort(sortedWires);

        int wireMask = 0;
        for(int wire : wires) {
            wireMask |= 1<<wire;
        }

        for(int row = 0; row < systemSize; row++)  {

            int rowNotWire = ~wireMask & row;
            int rowQubitsWire = 0;
            for(int i = wires.length-1; i >= 0; i--) {
                rowQubitsWire = (rowQubitsWire << 1) | ((row >> wires[i]) & 1);
            }

            for(int col = 0; col < systemSize; col++) {

                // Weird bit of code based on:
                // https://github.com/RoboNeo9/Java-Quantum-Computer-Simulator/blob/master/Core/MasterGate.java

                int colNotWire = ~wireMask & col;

                if(rowNotWire == colNotWire) {

                    int colQubitsWire = 0;
                    for(int i = wires.length-1; i >= 0; i--) {
                        colQubitsWire = (colQubitsWire << 1) | ((col >> wires[i]) & 1);
                    }

                    Complex data = gateData[rowQubitsWire][colQubitsWire];
                    masterGate.setEntry(row, col, data);
                }
            }
        }

//        System.out.println(masterGate.getRowDimension()+" x "+masterGate.getColumnDimension());

//        System.out.println("Applying gate:");
//        prettyPrintMatrix(masterGate);

        system = system.multiply(masterGate);

//        System.out.println("Updated system:");
//        prettyPrintMatrix(system);

        return this;
    }

    /**
     * Applies a controlled-U gate to the system.
     * @param gate The unitary gate U.
     * @param controlQubit The control qubit.
     * @param targetQubit The target qubit.
     */
    public QSystem applyControlledUGate(final FieldMatrix<Complex> gate, final int controlQubit, final int targetQubit) {
        int size = systemSize;
        Complex[][] cuMatrix = new Complex[size][size];

        // Initialize the controlled-U matrix as an identity matrix
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cuMatrix[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }

        // Apply the controlled gate
        for (int i = 0; i < size; i++) {
            if ((i & (1 << controlQubit)) != 0) { // Control qubit is 1
                int rowIndex = i ^ (1 << targetQubit);
                for (int j = 0; j < size; j++) {
                    if ((j & (1 << controlQubit)) != 0) { // Control qubit is 1
                        int colIndex = j ^ (1 << targetQubit);
                        cuMatrix[i][j] = gate.getEntry(rowIndex % gate.getRowDimension(), colIndex % gate.getColumnDimension());
                    }
                }
            }
        }

        FieldMatrix<Complex> controlledU = MatrixUtils.createFieldMatrix(cuMatrix);
        return applyGate(controlledU, controlQubit, targetQubit);
    }

    /**
     * Applies modular exponentiation to the system.
     * @param base The base for exponentiation.
     * @param modulus The modulus.
     * @param numQubits The number of qubits.
     */
    public QSystem applyModularExponentiation(int base, int modulus, int numQubits) {
        for (int i = 0; i < numQubits; i++) {
            int exponent = (int) Math.pow(base, Math.pow(2, i)) % modulus;
            FieldMatrix<Complex> modExpMatrix = createModExpMatrix(exponent, modulus, numQubits);
            applyControlledUGate(modExpMatrix, i, numQubits + i);
        }
        return this;
    }

    private FieldMatrix<Complex> createModExpMatrix(int exponent, int modulus, int numQubits) {
        int size = (int) Math.pow(2, numQubits);
        Complex[][] matrix = new Complex[size][size];
        for (int i = 0; i < size; i++) {
            int result = (i * exponent) % modulus;
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (j == result) ? Complex.ONE : Complex.ZERO;
            }
        }
        return MatrixUtils.createFieldMatrix(matrix);
    }

    /**
     * Applies the inverse Fourier gate to the system.
     */
    public QSystem applyInverseFourierGate(int qubits) {
        FieldMatrix<Complex> invQFT = FOURIER_GATE(qubits).transpose();
        return applyGate(invQFT, 0, qubits - 1);
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

    public static FieldMatrix<Complex> createEmptyMatrix(int rows, int cols) {
        Complex[][] initialMatrix = new Complex[rows][cols];
        for(int row = 0; row < rows; row++) Arrays.fill(initialMatrix[row], Complex.ZERO);
        return MatrixUtils.createFieldMatrix(initialMatrix);
    }

    public static void prettyPrintMatrix(final FieldMatrix<Complex> masterGate) {
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
