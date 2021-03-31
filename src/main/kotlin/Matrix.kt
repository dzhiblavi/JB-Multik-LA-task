interface ImmutableMatrix<T> {
    val rows: Int
    val columns: Int
    val size get() = rows * columns
    fun isEmpty() = size == 0

    operator fun get(i: Int, j: Int) : T
}

interface Matrix<T> : ImmutableMatrix<T> {
    operator fun set(i: Int, j: Int, value: T)
}

class ArrayIntMatrix(private val arr: Array<Array<Int>>) : Matrix<Int> {
    override val rows: Int
        get() = arr.size

    override val columns: Int
        get() = if (arr.isEmpty()) 0 else arr[0].size

    override fun get(i: Int, j: Int) : Int {
        require(i in 0 until rows && j in 0 until columns) { "Indices out of bounds" }
        return arr[i][j]
    }

    override fun set(i: Int, j: Int, value: Int) {
        require(i in 0 until rows && j in 0 until columns) { "Indices out of bounds" }
        arr[i][j] = value
    }

    operator fun plus(m: ArrayIntMatrix) : ArrayIntMatrix {
        return combine(m, Int::plus)
    }

    operator fun minus(m: ArrayIntMatrix) : ArrayIntMatrix {
        return combine(m, Int::minus)
    }

    operator fun times(m: ArrayIntMatrix) : ArrayIntMatrix {
        if (rows < 64 || columns < 64 || m.columns < 64)
            return multiplyMatricesNaive(this, m)
        return multiplyMatricesStrassen(this, m)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ArrayIntMatrix) return false
        return arr contentDeepEquals other.arr
    }

    fun getSubMatrix(iFrom: Int, iTo: Int, jFrom: Int, jTo: Int) : ArrayIntMatrix {
        require(iFrom <= iTo && jFrom <= jTo) { "From must not be greater than To" }
        require(iFrom >= 0 && jFrom >= 0) { "All indices must be non-negative" }
        require(iTo <= rows && jTo <= columns ) { "Indices must be inside the old matrix" }

        val result = ArrayIntMatrix(Array(iTo - iFrom) { Array(jTo - jFrom) { 0 } })

        for (i in iFrom until iTo)
            for (j in jFrom until jTo)
                result[i - iFrom, j - jFrom] = get(i, j)

        return result
    }

    fun copySubMatrix(iFrom: Int, iTo: Int, jFrom: Int, jTo: Int, a: ArrayIntMatrix) {
        require(iFrom <= iTo && jFrom <= jTo) { "From must not be greater than To" }
        require(iFrom >= 0 && jFrom >= 0) { "All indices must be non-negative" }
        require(a.rows == iTo - iFrom && a.columns == jTo - jFrom) { "Indices do not match" }
        require(iTo - iFrom <= rows && jTo - jFrom <= columns) { "Too large matrix to copy from" }

        for (i in iFrom until iTo)
            for (j in jFrom until jTo)
                set(i, j, a[i - iFrom, j - jFrom])
    }

    fun extendWithZeroes(newRows: Int, newColumns: Int) : ArrayIntMatrix {
        require(newRows >= rows && newColumns >= columns) { "New dimensions must be not smaller than the old ones" }

        val result = ArrayIntMatrix(Array(newRows) { Array(newColumns) { 0 } })

        for (i in 0 until rows)
            for (j in 0 until columns)
                result[i, j] = get(i, j)

        return result
    }

    private fun combine(m: ArrayIntMatrix, func : (Int, Int) -> Int) : ArrayIntMatrix {
        require(rows == m.rows && columns == m.columns) { "Matrices must have the same size" }

        val result = ArrayIntMatrix(Array(rows) { Array(columns) { 0 } })

        for (i in 0 until rows)
            for (j in 0 until columns)
                result[i, j] = func(get(i, j), m[i, j])

        return result
    }
}
