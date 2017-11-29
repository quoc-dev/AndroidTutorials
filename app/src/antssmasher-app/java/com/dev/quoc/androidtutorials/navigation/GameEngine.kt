package com.dev.quoc.androidtutorials.navigation

import android.os.Handler
import com.dev.quoc.androidtutorials.model.Ant
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/29/17.
 */
class GameEngine : Contract.GameEngine {

    private var gameView: Contract.GameView? = null

    private val ants: MutableCollection<Ant> = ArrayList()
    private val random: Random = Random()
    private var score = 0
    private val handler: Handler = Handler()
    private val showNewAntTask: Runnable = object : Runnable {
        override fun run() {
            val newAnt = createNewAnt()
            val isGameOver = checkIfGameIsOver()
            if (!isGameOver) {
                gameView?.showAnt(newAnt)
                ants.add(newAnt)
                handler.postDelayed(this, 1000)
            } else {
                handler.removeCallbacks(this)
                gameView?.clearView()
                gameView?.setGameOverTextVisibility(true)
                gameView?.setPlayButtonTextVisibility(true)
            }
        }
    }

    private fun checkIfGameIsOver(): Boolean {
        return ants.isNotEmpty()
    }

    override fun onGameViewReady(view: Contract.GameView) {
        gameView = view
        gameView?.clearView()
        gameView?.setIntroTextVisibility(true)
    }

    override fun onViewDestroyed() {
        handler.removeCallbacks(showNewAntTask)
        gameView = null
    }

    override fun onPlayButtonClicked() {
        gameView?.setPlayButtonTextVisibility(false)
        gameView?.clearView()
        // start new game
        ants.clear()
        showNewAntTask.run()
        score = 0
    }

    override fun onAntClicked(ant: Ant) {
        ants.remove(ant)
        gameView?.hideAnt(ant)
        score++
        gameView?.showScore(score)
    }

    private fun createNewAnt(): Ant {
        val x = getAntXPosition()
        val y = getAntYPosition()
        val id = ants.size + 1
        return Ant(id, x, y)
    }

    /**
     * returns Ant's vertical position as a screen height ratio, in range 0.0-1.0
     */
    private fun getAntYPosition(): Double {
        return random.nextDouble()
    }

    /**
     * returns Ant's horizontal position as a screen width ratio, in range 0.0-1.0
     */
    private fun getAntXPosition(): Double {
        return random.nextDouble()
    }
}
