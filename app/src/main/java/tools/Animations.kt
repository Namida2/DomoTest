package tools

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.OvershootInterpolator

class Animations {

    companion object {
        fun startAnimationViewShowing (view : View) {
            val objectAnimatorScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f, 1f)
            val objectAnimatorScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f, 1f)
            val objectAnimatorAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1f)
            ObjectAnimator.ofPropertyValuesHolder(view, objectAnimatorScaleX, objectAnimatorScaleY, objectAnimatorAlpha).apply {
                interpolator = OvershootInterpolator()
                duration = 180
            }.start()
        }
    }


}