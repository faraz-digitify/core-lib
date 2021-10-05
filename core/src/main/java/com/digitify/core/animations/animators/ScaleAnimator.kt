package com.digitify.core.animations.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.digitify.core.animations.Animator

class ScaleAnimator(
    val from: Float,
    val to: Float,
    private val scaleInterpolator: Interpolator? = LinearInterpolator()
) : Animator() {
    override fun with(view: View, duration: Long?): AnimatorSet =
        runTogether(
            ObjectAnimator.ofFloat(view, "scaleX", from, to),
            ObjectAnimator.ofFloat(view, "scaleY", from, to)
        ).apply {
            this.duration = duration!!
            this.interpolator = scaleInterpolator
        }
}