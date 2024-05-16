package com.royvanrijn.quantum;

import java.math.BigInteger;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
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
     * π/8 gate (T gate) also known as Z^1/4
     */
    public static final FieldMatrix<Complex> T_GATE = createZGate(4);

    /**
     * create any Z^1/n gate
     */
    public static final FieldMatrix<Complex> createZGate(int fraction) {
        return MatrixUtils.createFieldMatrix(new Complex[][]{
                {Complex.ONE, Complex.ZERO},
                {Complex.ZERO, new Complex(0, Math.PI / fraction).exp()}
        });
    }

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


    // Method to create a controlled gate with flexible order
    public static FieldMatrix<Complex> createControlledGate(FieldMatrix<Complex> operation, int[] operationWires, int[] controlWires) {
        int totalQubits = operationWires.length + controlWires.length;
        int dim = 1 << totalQubits;
        FieldMatrix<Complex> result = new Array2DRowFieldMatrix<>(ComplexField.getInstance(), dim, dim);

        // Fill result with identity
        for (int i = 0; i < dim; i++) {
            result.setEntry(i, i, Complex.ONE);
        }

        // Calculate the dimension of the operation space
        int operationDim = 1 << operationWires.length;

        // Overwrite last entries with operation
        for (int i = 0; i < operationDim; i++) {
            for (int j = 0; j < operationDim; j++) {
                // Index in the original order
                int originalI = 0;
                int originalJ = 0;
                // Embed operation qubits
                for (int k = 0; k < operationWires.length; k++) {
                    originalI |= ((i >> k) & 1) << operationWires[k];
                    originalJ |= ((j >> k) & 1) << operationWires[k];
                }
                // Embed control qubits (control bit = 1)
                for (int k : controlWires) {
                    originalI |= 1 << k;
                    originalJ |= 1 << k;
                }
                result.setEntry(originalI, originalJ, operation.getEntry(i, j));
            }
        }

        return result;
    }

    public static FieldMatrix<Complex> kroneckerProduct(FieldMatrix<Complex>... matrixes) {

        int newRow = 1;
        int newCol = 1;
        for(FieldMatrix<Complex> ma : matrixes) {
            newRow *= ma.getRowDimension();
            newCol *= ma.getColumnDimension();
        }

        // create a matrix to hold the data
        Complex[][] productData = new Complex[newRow][newCol];
        // initialize to 1 (allows us to multiple the contents of each matrix
        // onto the result sequentially
        for (int prodRow = 0; prodRow < newRow; prodRow++) {
            for (int prodCol = 0; prodCol < newCol; prodCol++) {
                productData[prodRow][prodCol] = Complex.ONE;
            }
        }

        // multiply the contents of each matrix onto the result
        int maxRow = newRow;
        int maxCol = newCol;
        for (FieldMatrix<Complex> matrix : matrixes) {
            maxRow /= matrix.getRowDimension();
            maxCol /= matrix.getColumnDimension();
            int matrixRow = 0;
            int matrixCol = 0;
            // multiply onto the result
            for (int prodRow = 0, sectionRow = 0; prodRow < newRow; prodRow++, sectionRow++) {
                matrixCol = 0;
                Complex value = matrix.getEntry(matrixRow, matrixCol);
                for (int prodCol = 0, sectionCol = 0; prodCol < newCol; prodCol++, sectionCol++) {
                    productData[prodRow][prodCol] = productData[prodRow][prodCol].multiply(value);
                    if (sectionCol >= maxCol - 1) {
                        matrixCol++;
                        if (matrixCol >= matrix.getColumnDimension())
                            matrixCol = 0;
                        sectionCol = -1;
                        value = matrix.getEntry(matrixRow, matrixCol);
                    }
                }
                if (sectionRow >= maxRow - 1) {
                    matrixRow++;
                    if (matrixRow >= matrix.getRowDimension())
                        matrixRow = 0;
                    sectionRow = -1;
                }
            }
        }

        // return a new matrix containing the Kronecker product
        return MatrixUtils.createFieldMatrix(productData);
    }


    public static FieldMatrix<Complex> bPowAmodR(int n, int R, int b) {

        FieldMatrix<Complex> modularExponentiationMatrix = QSystem.createEmptyMatrix(1<<n, 1<<n);

        // Fill in the matrix with the modular exponentiation operation
        for (int a = 0; a < (1 << n); a++) {
            int result = modPow(b, a, R); // Assuming modPow is a function that computes (b^a) mod R efficiently
            modularExponentiationMatrix.setEntry(result, a, Complex.ONE);
        }
        return modularExponentiationMatrix;

    }

    // modPow could be implemented in Java using BigInteger for large numbers, for instance
    public static int modPow(int base, int exponent, int modulus) {
        return BigInteger.valueOf(base)
                .modPow(BigInteger.valueOf(exponent), BigInteger.valueOf(modulus))
                .intValue();
    }



}
