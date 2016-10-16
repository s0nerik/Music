package com.github.s0nerik.music.ui.views

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringSystem
import com.github.s0nerik.music.databinding.FragmentNowPlayingBinding
import com.github.s0nerik.music.players.PlayerController
import com.github.s0nerik.music.screens.playback.PlaybackActivity
import kotlinx.android.synthetic.main.fragment_now_playing.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.onClick
import rx.Observable

class NowPlayingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: FragmentNowPlayingBinding

    var playerController: PlayerController? = null
        set(value) {
            @Suppress("MISSING_DEPENDENCY_CLASS")
            binding.playerController = value
        }

    var progress: Float = 0f
        get
        set(value) {
            field = value
            val circleRect = Rect()
            playbackFab.getGlobalVisibleRect(circleRect)
            val circleStartPercent = 100f - (mainProgress.width - circleRect.left) / mainProgress.width.toFloat() * 100f
            val circleEndPercent = 100f - (mainProgress.width - circleRect.right) / mainProgress.width.toFloat() * 100f
            if (progress > circleStartPercent && progress < circleEndPercent) {
                val circlePercents = circleEndPercent - circleStartPercent
                circleProgress.progress = ((progress - circleStartPercent) / circlePercents) * 100f
                mainProgress.progress = (((circleEndPercent + circleStartPercent) / 2f).toInt() * 10f).toInt()
            } else if (progress <= circleStartPercent) {
                mainProgress.progress = (progress * 10f).toInt()
                circleProgress.progress = 0f
            } else if (progress >= circleEndPercent) {
                mainProgress.progress = (progress * 10f).toInt()
                circleProgress.progress = 100f
            }
        }

    init {
        binding = FragmentNowPlayingBinding.inflate(context.layoutInflater)
        addView(binding.root)

        listOf(layout, cover).forEach {
            it.onClick {
                with(context){ startActivity(intentFor<PlaybackActivity>()) }
            }
        }
    }

    fun show(): Observable<Int> {
        return Observable.create({
            visibility = View.INVISIBLE

            val mainSpringSystem = SpringSystem.create()
            val mainSpring = mainSpringSystem.createSpring()
            mainSpring.springConfig = SpringConfig(1000.0, 100.0)
            mainSpring.addListener(object : SimpleSpringListener() {
                override fun onSpringActivate(spring: Spring) {
                    visibility = View.VISIBLE
                    fragment.translationY = 100000f
                    mainProgressShadow.alpha = 0f

                    scaleFab(0f)
                }

                override fun onSpringUpdate(s: Spring) {
                    val translation = (1f - s.currentValue) * mainGroup.height

                    fragment.translationY = translation.toFloat()
                    mainProgressShadow.alpha = s.currentValue.toFloat()
                }

                override fun onSpringAtRest(spr: Spring) {
                    val springSystem = SpringSystem.create()
                    val spring = springSystem.createSpring()
                    spring.addListener(object : SimpleSpringListener() {
                        override fun onSpringUpdate(s: Spring) {
                            scaleFab(s.currentValue.toFloat())
                        }

                        override fun onSpringAtRest(s: Spring) {
                            it.onNext(mainGroup.height - mainGroup.paddingTop)
                            it.onCompleted()
                        }

                    })
                    spring.endValue = 1.0
                }
            })
            mainSpring.endValue = 1.0
        })
    }

    private fun scaleFab(scale: Float) {
        btnPlayPause.scaleX = scale
        btnPlayPause.scaleY = scale

        circleProgressBg.scaleX = scale
        circleProgressBg.scaleY = scale
        circleProgressBg.alpha = scale

        circleProgressShadow.scaleX = scale
        circleProgressShadow.scaleY = scale
        circleProgressShadow.alpha = scale
    }
}

@BindingAdapter("nowPlayingProgressPercent")
fun setNowPlayingProgressPercent(view: NowPlayingView, progress: Float) {
    view.progress = progress
}