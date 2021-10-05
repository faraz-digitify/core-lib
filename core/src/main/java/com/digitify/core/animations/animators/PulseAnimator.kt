package com.digitify.core.animations.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.digitify.core.utils.AnimationUtils
import com.digitify.core.animations.Animator

class PulseAnimator : Animator() {
    override fun with(view: View, duration: Long?): AnimatorSet = AnimationUtils.runTogether(
        ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 0.95f, 1f),
        ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 0.95f, 1f)
    ).apply { this.duration = duration!! }
}