import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class MatMulTest {
    private val rand = Random(239301337)

    private fun randomMatrix(rows: Int, columns: Int) : ArrayIntMatrix {
        return ArrayIntMatrix(Array(rows) { Array(columns) { rand.nextInt(100) } })
    }

    private fun testEmptyMatrices(mulOp: (ArrayIntMatrix, ArrayIntMatrix) -> ArrayIntMatrix) {
        assertEquals(
            mulOp(ArrayIntMatrix(arrayOf()),
                ArrayIntMatrix(arrayOf())),
            ArrayIntMatrix(arrayOf())
        )
    }

    private fun testOneEmptyMatrix(mulOp: (ArrayIntMatrix, ArrayIntMatrix) -> ArrayIntMatrix) {
        assertFails {
            mulOp(ArrayIntMatrix(arrayOf()),
                ArrayIntMatrix(arrayOf(arrayOf(0, 1))))
        }
    }

    private fun testSizeMismatch(mulOp: (ArrayIntMatrix, ArrayIntMatrix) -> ArrayIntMatrix) {
        assertFails {
            mulOp(ArrayIntMatrix(
                arrayOf(
                    arrayOf(1, 2),
                    arrayOf(4, 5),
                    arrayOf(7, 8))),
                ArrayIntMatrix(
                    arrayOf(
                        arrayOf(7, 8),
                        arrayOf(9, 10),
                        arrayOf(11, 12))))
        }
    }

    private fun testSmallTests(mulOp: (ArrayIntMatrix, ArrayIntMatrix) -> ArrayIntMatrix) {
        assertEquals(
            ArrayIntMatrix(arrayOf(arrayOf(6))),
            mulOp(ArrayIntMatrix(arrayOf(arrayOf(2))),
                ArrayIntMatrix(arrayOf(arrayOf(3))))
        )
        assertEquals(
            ArrayIntMatrix(
                arrayOf(
                    arrayOf(58, 64),
                    arrayOf(139, 154))),
            mulOp(ArrayIntMatrix(
                arrayOf(
                    arrayOf(1, 2, 3),
                    arrayOf(4, 5, 6))),
                ArrayIntMatrix(
                    arrayOf(
                        arrayOf(7, 8),
                        arrayOf(9, 10),
                        arrayOf(11, 12))))
        )
    }

    private fun testLargeTestsStrassen() {
        repeat(10) {
            val aRows = rand.nextInt(10, 1000)
            val aColumns = rand.nextInt(10, 1000)
            val bColumns = rand.nextInt(10, 1000)
            val a = randomMatrix(aRows, aColumns)
            val b = randomMatrix(aColumns, bColumns)

            assertEquals(
                multiplyMatricesNaive(a, b),
                multiplyMatricesStrassen(a, b)
            )
        }
    }

    @Test
    fun empty() {
        testEmptyMatrices(::multiplyMatricesNaive)
        testEmptyMatrices(::multiplyMatricesStrassen)
    }

    @Test
    fun oneEmpty() {
        testOneEmptyMatrix(::multiplyMatricesNaive)
        testOneEmptyMatrix(::multiplyMatricesStrassen)
    }

    @Test
    fun sizeMismatch() {
        testSizeMismatch(::multiplyMatricesNaive)
        testSizeMismatch(::multiplyMatricesStrassen)
    }

    @Test
    fun smallTests() {
        testSmallTests(::multiplyMatricesNaive)
        testSmallTests(::multiplyMatricesStrassen)
    }

    @Test
    fun largeTests() {
        testLargeTestsStrassen()
    }
}