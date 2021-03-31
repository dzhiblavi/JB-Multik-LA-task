/**
 * Multiplies two given integer matrices.
 *
 * Number of columns of first matrix should be equal
 * to number of rows in the second matrix.
 *
 * @param a the first matrix
 * @param b the second matrix
 * @throws IllegalArgumentException if input matrices won't match by size
 * @return the result of multiplication
 */
fun multiplyMatricesNaive(a : ArrayIntMatrix, b : ArrayIntMatrix): ArrayIntMatrix {
    if (a.isEmpty() && b.isEmpty()) return ArrayIntMatrix(arrayOf())
    require(a.columns == b.rows) { "Matrices do not match by size" }

    val result = ArrayIntMatrix(Array(a.rows) { Array(b.columns) { 0 } })

    for (i in 0 until result.rows)
        for (j in 0 until result.columns)
            for (k in 0 until a.columns)
                result[i, j] = result[i, j] + a[i, k] * b[k, j]

    return result
}

/**
 * @see multiplyMatricesNaive
 */
fun multiplyMatricesStrassen(a: ArrayIntMatrix, b: ArrayIntMatrix): ArrayIntMatrix {
    if (a.isEmpty() && b.isEmpty()) return ArrayIntMatrix(arrayOf())
    require(a.columns == b.rows) { "Matrices do not match by size" }

    val ma = prepareMatrix(a)
    val mb = prepareMatrix(b)
    val res = multiplyMatricesStrassenImpl(ma, mb)

    return res.getSubMatrix(0, a.rows, 0, b.columns)
}

