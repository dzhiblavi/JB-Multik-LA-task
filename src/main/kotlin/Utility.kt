fun isPowerOfTwo(x: Int) : Boolean {
    return x != 0 && (x and (x - 1)) == 0
}

fun findUpperPowerOfTwo(x: Int) : Int {
    var d = 1
    while (d < x) d *= 2
    return d
}

fun prepareMatrix(a: ArrayIntMatrix) : ArrayIntMatrix {
    if (isPowerOfTwo(a.rows) && isPowerOfTwo(a.columns)) return a
    val newRows = findUpperPowerOfTwo(a.rows)
    val newColumns = findUpperPowerOfTwo(a.columns)
    return a.extendWithZeroes(newRows, newColumns)
}

fun multiplyMatricesStrassenImpl(a: ArrayIntMatrix, b: ArrayIntMatrix): ArrayIntMatrix {
    if (a.rows < 64 || a.columns < 64 || b.columns < 64)
        return multiplyMatricesNaive(a, b)

    val aHalfI = a.rows / 2
    val aHalfJ = a.columns / 2
    val bHalfI = b.rows / 2
    val bHalfJ = b.columns / 2

    val a11 = a.getSubMatrix(0, aHalfI, 0, aHalfJ)
    val a12 = a.getSubMatrix(0, aHalfI, aHalfJ, a.columns)
    val a21 = a.getSubMatrix(aHalfI, a.rows, 0, aHalfJ)
    val a22 = a.getSubMatrix(aHalfI, a.rows, aHalfJ, a.columns)

    val b11 = b.getSubMatrix(0, bHalfI, 0, bHalfJ)
    val b12 = b.getSubMatrix(0, bHalfI, bHalfJ, b.columns)
    val b21 = b.getSubMatrix(bHalfI, b.rows, 0, bHalfJ)
    val b22 = b.getSubMatrix(bHalfI, b.rows, bHalfJ, b.columns)

    val p1 = multiplyMatricesStrassenImpl(a11 + a22, b11 + b22)
    val p2 = multiplyMatricesStrassenImpl(a21 + a22, b11)
    val p3 = multiplyMatricesStrassenImpl(a11, b12 - b22)
    val p4 = multiplyMatricesStrassenImpl(a22, b21 - b11)
    val p5 = multiplyMatricesStrassenImpl(a11 + a12, b22)
    val p6 = multiplyMatricesStrassenImpl(a21 - a11, b11 + b12)
    val p7 = multiplyMatricesStrassenImpl(a12 - a22, b21 + b22)

    val c11 = p1 + p4 - p5 + p7
    val c12 = p3 + p5
    val c21 = p2 + p4
    val c22 = p1 - p2 + p3 + p6

    val c = ArrayIntMatrix(Array(a.rows) { Array(b.columns) { 0 } })
    val cHalfI = c.rows / 2
    val cHalfJ = c.columns / 2

    c.copySubMatrix(0, cHalfI, 0, cHalfJ, c11)
    c.copySubMatrix(0, cHalfI, cHalfJ, c.columns, c12)
    c.copySubMatrix(cHalfI, c.rows, 0, cHalfJ, c21)
    c.copySubMatrix(cHalfI, c.rows, cHalfJ, c.columns, c22)

    return c
}

