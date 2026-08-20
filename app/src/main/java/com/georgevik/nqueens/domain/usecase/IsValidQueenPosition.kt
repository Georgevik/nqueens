package com.georgevik.nqueens.domain.usecase

import com.georgevik.nqueens.domain.model.Position
import javax.inject.Inject

class IsValidQueenPosition @Inject constructor() {

    operator fun invoke(queen: Position, nQueenSize: Int, boardQueens: Set<Position>): Boolean {
        val others = boardQueens - queen

        return (0 until nQueenSize).none { i ->
            Position(queen.col - i, queen.row) in others ||
                    Position(queen.col - i, queen.row + i) in others ||
                    Position(queen.col, queen.row + i) in others ||
                    Position(queen.col + i, queen.row + i) in others ||
                    Position(queen.col + i, queen.row) in others ||
                    Position(queen.col + i, queen.row - i) in others ||
                    Position(queen.col, queen.row - i) in others ||
                    Position(queen.col - i, queen.row - i) in others
        }
    }
}
