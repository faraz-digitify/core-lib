package com.digitify.core.animations.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.digitify.core.animations.Animator

class FadeInAnimator : Animator() {
    override fun with(view: View, duration: Long?): AnimatorSet =
        runTogether(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)).apply {
            this.duration = duration!!
        }
}