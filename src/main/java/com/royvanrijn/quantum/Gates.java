package com.royvanrijn.quantum;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

/**
 * All quantum gates can be simulated by "simple" matrix operations on Complex numbers.
 *
 * This class implements most common quantum gates.
 */
public class Gates {

    /**
     * Identity gate
     */
    public static final FieldMatrix<Complex> IDENTITY_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ONE, Complex.ZERO},
                    {Complex.ZERO, Complex.ONE}
            });

    /**
     * Pauli-X gate (NOT):
     */
    public static final FieldMatrix<Complex> PAULI_X_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ZERO, Complex.ONE},
                    {Complex.ONE, Complex.ZERO}
            });

    /**
     * Pauli-Y gate (different NOT):
     */
    public static final FieldMatrix<Complex> PAULI_Y_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ZERO, Complex.I.negate()},
                    {Complex.I, Complex.ZERO}
            });

    /**
     * Pauli-Z gate
     */
    public static final FieldMatrix<Complex> PAULI_Z_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ONE, Complex.ZERO},
                    {Complex.ZERO, Complex.ONE.negate()}
            });

    /**
     * Hadamard gate
     */
    public static final FieldMatrix<Complex> HADAMARD_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {new Complex(1/Math.sqrt(2)),new Complex(1/Math.sqrt(2))},
                    {new Complex(1/Math.sqrt(2)),new Complex(-1/Math.sqrt(2))}
            });


    /**
     * Phase gate (S-Gate)
     */
    public static final FieldMatrix<Complex> PHASE_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ONE, Complex.ZERO},
                    {Complex.ZERO, Complex.I }
            });


    /**
     * π/8 gate (T gate)
     */
    public static final FieldMatrix<Complex> T_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ONE, Complex.ZERO},
                    {Complex.ZERO, new Complex(0, Math.PI / 4).exp()}
            });


    /**
     * Swap Gate (swap the value of two qubits.
     */
    public static final FieldMatrix<Complex> SWAP_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ZERO},
                    {Complex.ZERO, Complex.ZERO, Complex.ONE, Complex.ZERO},
                    {Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ZERO},
                    {Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE},
            });


    /**
     * C-NOT gate
     */
    public static final FieldMatrix<Complex> CNOT_GATE =
            MatrixUtils.createFieldMatrix(new Complex[][] {
                    {Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ZERO},
                    {Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ZERO},
                    {Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE},
                    {Complex.ZERO, Complex.ZERO, Complex.ONE, Complex.ZERO},
            });
    //TODO: Create a 'generic' control gate so it can be combined with other gates
    //TODO: This is built up using identity and control.

    /**
     * Quantum Fourier gate
     */
    public static final FieldMatrix<Complex> FOURIER_GATE(int qubits) {
        int size = (int) Math.pow(2, qubits);
        Complex[][] gate = new Complex[size][size];

        Complex root = new Complex(0,(2*Math.PI)/size).exp(); //nth root of unity

        for(int i=0;i<size;i++) {
            for(int j=0;j<size;j++) {
                gate[i][j] = root.pow((i*j)%size).multiply(Math.pow(size, -0.5));
            }
        }
        return MatrixUtils.createFieldMatrix(gate);
    }

}
