package com.dev.quoc.androidtutorials.navigation

import com.dev.quoc.androidtutorials.model.Ant

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/29/17.
 */
interface Contract {

    interface GameView {

        fun showAnt(ant: Ant)

        fun hideAnt(ant: Ant)

        fun showScore(score: Int)

        fun setIntroTextVisibility(visibility: Boolean)

        fun setGameOverTextVisibility(visibility: Boolean)

        fun setPlayButtonTextVisibility(visibility: Boolean)

        fun clearView()
    }

    interface GameEngine {

        fun onGameViewReady(view: GameView)

        fun onViewDestroyed()

        fun onPlayButtonClicked()

        fun onAntClicked(ant: Ant)
    }
}
