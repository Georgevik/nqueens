package com.georgevik.nqueens.domain.usecase

import com.georgevik.nqueens.domain.model.Position
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsValidQueenPositionTest {

    private val underTest = IsValidQueenPosition()

    @Test
    fun `returns true WHEN the board is empty`() {
        val result = underTest(Position(0, 0), nQueenSize = 8, boardQueens = emptySet())

        assertTrue(result)
    }

    @Test
    fun `returns false WHEN another queen shares the row`() {
        val result = underTest(
            queen = Position(col = 3, row = 0),
            nQueenSize = 8,
            boardQueens = setOf(Position(col = 0, row = 0)),
        )

        assertFalse(result)
    }

    @Test
    fun `returns false WHEN another queen shares the column`() {
        val result = underTest(
            queen = Position(col = 0, row = 3),
            nQueenSize = 8,
            boardQueens = setOf(Position(col = 0, row = 0)),
        )

        assertFalse(result)
    }

    @Test
    fun `returns false WHEN another queen is on the diagonal`() {
        val result = underTest(
            queen = Position(col = 3, row = 3),
            nQueenSize = 8,
            boardQueens = setOf(Position(col = 0, row = 0)),
        )

        assertFalse(result)
    }

    @Test
    fun `returns true WHEN no queen attacks the position`() {
        val result = underTest(
            queen = Position(col = 1, row = 2),
            nQueenSize = 8,
            boardQueens = setOf(Position(col = 0, row = 0)),
        )

        assertTrue(result)
    }

    @Test
    fun `returns true WHEN the queen itself is the only one on the board`() {
        val result = underTest(
            queen = Position(col = 2, row = 2),
            nQueenSize = 8,
            boardQueens = setOf(Position(col = 2, row = 2)),
        )

        assertTrue(result)
    }
}
