package com.royvanrijn.quantum;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;

public class ExtraMatrixUtils {

    /**
     * Apply a tensorProduct product to a matrix
     * @param lhs
     * @param rhs
     * @return
     */
    public static FieldMatrix<Complex> tensorProduct(FieldMatrix<Complex> lhs, FieldMatrix<Complex> rhs) {
        int r1 = lhs.getRowDimension();
        int c1 = lhs.getColumnDimension();
        int r2 = rhs.getRowDimension();
        int c2 = rhs.getColumnDimension();
        FieldMatrix<Complex> result = new Array2DRowFieldMatrix<>(ComplexField.getInstance(),
                r1 * r2, c1 * c2);
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c1; j++) {
                for (int k = 0; k < r2; k++) {
                    for (int l = 0; l < c2; l++) {
                        int row = i * r2 + k, col = j * c2 + l;
                        result.setEntry(row, col, lhs.getEntry(i, j).multiply(rhs.getEntry(k, l)));
                    }
                }
            }
        }
        return result;
    }
}
