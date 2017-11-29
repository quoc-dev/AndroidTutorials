package com.dev.quoc.androidtutorials

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.dev.quoc.androidtutorials.model.Ant
import com.dev.quoc.androidtutorials.navigation.Contract
import com.dev.quoc.androidtutorials.navigation.GameEngine
import kotlinx.android.synthetic.`antssmasher-app`.activity_ants_smasher.*

class AntsSmasherActivity : AppCompatActivity(), Contract.GameView {

    private val engine: GameEngine = GameEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ants_smasher)

        btnPlay.setOnClickListener {
            engine.onPlayButtonClicked()
        }

        setBackground()

        engine.onGameViewReady(this)
    }

    private fun setBackground() {
        val bg = Drawable.createFromStream(assets.open("bg.jpeg"), null)
        frGameBoard.background = bg
    }

    override fun showAnt(ant: Ant) {
        val antView = ImageView(this)
        antView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ant))
        antView.scaleType = ImageView.ScaleType.FIT_CENTER
        antView.tag = ant
        antView.setOnClickListener {
            engine.onAntClicked(ant)
        }

        val antSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, resources.displayMetrics)

        val layoutParams = FrameLayout.LayoutParams(antSize.toInt(), antSize.toInt())
        val screenWidth = frGameBoard.width - antSize
        val screenHeight = frGameBoard.height - antSize
        layoutParams.leftMargin = (ant.x * screenWidth).toInt()
        layoutParams.topMargin = (ant.y * screenHeight).toInt()
        frGameBoard?.addView(antView, layoutParams)
    }

    override fun hideAnt(ant: Ant) {
        val antViewsCount: Int? = frGameBoard?.childCount

        antViewsCount?.let {
            for (i: Int in 0..antViewsCount) {
                val view = frGameBoard.getChildAt(i)
                val tempAnt = view?.tag
                if (ant == tempAnt) {
                    frGameBoard.removeView(view)
                    break
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun showScore(score: Int) {
        this.tvScore.text = "Points " + score
    }

    override fun setIntroTextVisibility(visibility: Boolean) {
        tvIntro.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun setGameOverTextVisibility(visibility: Boolean) {
        tvGameOver.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun setPlayButtonTextVisibility(visibility: Boolean) {
        btnPlay.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun clearView() {
        frGameBoard.removeAllViews()
        tvIntro.visibility = View.GONE
        tvGameOver.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.engine.onViewDestroyed()
    }
}
