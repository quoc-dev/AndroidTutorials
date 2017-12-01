package com.dev.quoc.androidtutorials

import org.jetbrains.annotations.Nullable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 12/1/17.
 *
 * <p>
 * TicTacToe coordinates for each square #SquareCoordinates
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * ------------------
 */
class TicTacToe {
    private var board: Array<IntArray>? = null

    var playerToMove = BoardPlayer.PLAYER_X
        private set // stores whose turn it is

    @Nullable
    private var ticTacToeListener: TicTacToeListener? = null
    private var numberOfMoves = 0

    init {
        initGame()
    }

    fun setTicTacToeListener(ticTacToeListener: TicTacToeListener) {
        this.ticTacToeListener = ticTacToeListener
    }

    fun isValidMove(x: Int, y: Int): Boolean {
        return board!![x][y] == SPACE
    }

    /*fun moveAt((x in 0..2 , y 0..2): Boolean {
        if (x < 0 || x > BOARD_ROW - 1 || y < 0 || y > BOARD_COLUMN - 1) {
            throw IllegalArgumentException(String.format("Coordinates %d and %d are not valid, valid set [0,1,2]", x, y))
        }
        if (!isValidMove(x, y)) {
            return false
        }
        numberOfMoves++
        if (ticTacToeListener != null) {
            ticTacToeListener!!.movedAt(x, y, playerToMove.move)
        }
        board!![x][y] = playerToMove.move
        val (first, second) = hasWon(x, y, playerToMove)
        if (first && ticTacToeListener != null) {
            ticTacToeListener!!.gameWonBy(playerToMove, second)
        } else if (numberOfMoves == BOARD_COLUMN * BOARD_ROW && ticTacToeListener != null) {
            ticTacToeListener!!.gameEndsWithATie()
        }
        changeTurnToNextPlayer()
        return true
    }*/

   /* private fun hasWon(x: Int, y: Int, playerToMove: BoardPlayer): Pair<Boolean, Array<SquareCoordinates>> {
        val winCoordinates = arrayOfNulls<SquareCoordinates>(3)
        val hasWon = (checkRow(x, y, playerToMove.move, winCoordinates)
                || checkColumn(x, y, playerToMove.move, winCoordinates)
                || checkDiagonals(x, y, playerToMove.move, winCoordinates))
        return Pair.create(hasWon, winCoordinates)
    }
*/
    private fun checkDiagonals(x: Int, y: Int, move: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        if (board!![0][0] == move && board!![1][1] == move && board!![2][2] == move) {
            winCoordinates[0] = SquareCoordinates(0, 0)
            winCoordinates[1] = SquareCoordinates(1, 1)
            winCoordinates[2] = SquareCoordinates(2, 2)
            return true
        } else if (board!![0][2] == move && board!![1][1] == move && board!![2][0] == move) {
            winCoordinates[0] = SquareCoordinates(0, 2)
            winCoordinates[1] = SquareCoordinates(1, 1)
            winCoordinates[2] = SquareCoordinates(2, 0)
            return true
        }
        return false
    }

    private fun checkColumn(x: Int, y: Int, movetoCheck: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        for (i in 0 until BOARD_ROW) {
            if (board!![i][y] != movetoCheck) {
                return false
            }
        }
        for (i in winCoordinates.indices) {
            winCoordinates[i] = SquareCoordinates(i, y)
        }
        return true
    }

    private fun checkRow(x: Int, y: Int, movetoCheck: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        for (i in 0 until BOARD_ROW) {
            if (board!![x][i] != movetoCheck) {
                return false
            }
        }
        for (i in winCoordinates.indices) {
            winCoordinates[i] = SquareCoordinates(x, i)
        }
        return true
    }

    private fun changeTurnToNextPlayer() {
        if (playerToMove == BoardPlayer.PLAYER_X) {
            playerToMove = BoardPlayer.PLAYER_O
        } else {
            playerToMove = BoardPlayer.PLAYER_X
        }
    }

    private fun initGame() {
        board = Array(BOARD_ROW) { IntArray(BOARD_COLUMN) }
        playerToMove = BoardPlayer.PLAYER_X
        numberOfMoves = 0
    }

    fun resetGame() {
        initGame()
    }

    fun getMoveAt(x: Int, y: Int): Int {
        return if (board!![x][y] == SPACE) {
            SPACE
        } else if (board!![x][y] == MOVE_O) {
            MOVE_O
        } else {
            MOVE_X
        }
    }

    companion object {
        private const val SPACE = 0
        private const val MOVE_X = 1
        private const val MOVE_O = 2

        private const val BOARD_ROW = 3
        private const val BOARD_COLUMN = 3
    }

    enum class BoardPlayer(move: Int) {
        PLAYER_X(MOVE_X), PLAYER_O(MOVE_O);

        var move = SPACE

        init {
            this.move = move
        }
    }

    interface TicTacToeListener {
        fun gameWonBy(boardPlayer: BoardPlayer, winPoints: Array<SquareCoordinates>)

        fun gameEndsWithATie()

        fun movedAt(x: Int, y: Int, move: Int)
    }

    // todo use this for passing coordinates
    class SquareCoordinates(val i: Int // holds the row index of a Square on Board
                            , val j: Int // holds the column index of a Square on Board
    ) {

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o == null || javaClass != o.javaClass) {
                return false
            }

            val that = o as SquareCoordinates?

            return if (i != that!!.i) {
                false
            } else j == that.j
        }

        override fun hashCode(): Int {
            var result = i
            result = 31 * result + j
            return result
        }
    }
}