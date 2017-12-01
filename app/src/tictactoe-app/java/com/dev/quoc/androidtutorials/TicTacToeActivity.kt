package com.dev.quoc.androidtutorials

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dev.quoc.androidtutorials.view.TicTacToeView
import kotlinx.android.synthetic.`tictactoe-app`.activity_tic_tac_toe.*

class TicTacToeActivity : AppCompatActivity(), TicTacToeJava.TicTacToeListener,
        TicTacToeView.SquarePressedListener {

    lateinit var ticTacToe: TicTacToeJava

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        ticTacToe = TicTacToeJava()
        ticTacToe.setTicTacToeListener(this)
        ticTacToeView.squarePressListener = this

        resetButton.setOnClickListener {
            ticTacToe.resetGame()
            resetGameUi()
            resetButton.visibility = View.GONE
        }
    }

    override fun onSquarePressed(i: Int, j: Int) {
        ticTacToe.moveAt(i, j)
    }


    override fun movedAt(x: Int, y: Int, move: Int) {
        if (move == TicTacToeJava.BoardState.MOVE_X)
            ticTacToeView.drawXAtPosition(x, y)
        else
            ticTacToeView.drawOAtPosition(x, y)
    }

    override fun gameEndsWithATie() {
        information.visibility = View.VISIBLE
        information.text = getString(R.string.game_ends_draw)
        resetButton.visibility = View.VISIBLE
        ticTacToeView.isEnabled = false
    }

    private fun resetGameUi() {
        ticTacToeView.reset()
        ticTacToeView.isEnabled = true
        information.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    override fun gameWonBy(boardPlayer: TicTacToeJava.BoardPlayer, winPoints: Array<TicTacToeJava.SquareCoordinates>) {
        information.visibility = View.VISIBLE
        information.text = "Winner is ${if (boardPlayer.move == TicTacToeJava.BoardState.MOVE_X) "X" else "O"}"
        ticTacToeView.animateWin(winPoints)
        ticTacToeView.isEnabled = false
        resetButton.visibility = View.VISIBLE
    }
}
