package yamoney.ru.customlayoutmanager

import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v7.widget.RecyclerView

val RecyclerView.ItemAnimator.ItemHolderInfo.width get() = this.right - this.left
val RecyclerView.ItemAnimator.ItemHolderInfo.height get() = this.bottom - this.top
val RecyclerView.ItemAnimator.ItemHolderInfo.centerX get() = (this.right - this.left) / 2 + this.left
val RecyclerView.ItemAnimator.ItemHolderInfo.centerY get() = (this.bottom - this.top) / 2 + this.top

abstract class ViewAnimation(
        private val viewHolder: RecyclerView.ViewHolder,
        private val onAnimationsEnd: (RecyclerView.ViewHolder) -> Unit) {

    private val animations = mutableListOf<SpringAnimation>()

    val isEmpty get() = animations.isEmpty()

    private fun add(anim: SpringAnimation) {
        anim.addEndListener { animation, _, _, _ ->
            animations.remove(animation)
            checkFinished()
        }
        animations.add(anim)
    }

    private fun checkFinished() {
        if (animations.isEmpty()) {
            onAnimationsEnd(viewHolder)
        }
    }

    fun skipToEnd() {
        animations.forEach { it.skipToEnd() }
    }

    fun start() {
        animations.forEach { it.start() }
    }

    fun translateX(startValue: Float, endValue: Float = 0f) {
        if (startValue != endValue) {
            addSpringAnimation(DynamicAnimation.TRANSLATION_X, endValue) {
                viewHolder.itemView.translationX = startValue
            }
        }
    }

    fun translateY(startValue: Float, endValue: Float = 0f) {
        if (startValue != endValue) {
            addSpringAnimation(DynamicAnimation.TRANSLATION_Y, endValue) {
                viewHolder.itemView.translationY = startValue
            }
        }
    }

    fun scaleX(startValue: Float, endValue: Float = 1f) {
        if (startValue != endValue) {
            addSpringAnimation(DynamicAnimation.SCALE_X, endValue) {
                viewHolder.itemView.scaleX = startValue
            }
        }
    }

    fun scaleY(startValue: Float, endValue: Float = 1f) {
        if (startValue != endValue) {
            addSpringAnimation(DynamicAnimation.SCALE_Y, endValue) {
                viewHolder.itemView.scaleX = startValue
            }
        }
    }

    fun alpha(startValue: Float, endValue: Float) {
        if (startValue != endValue) {
            addSpringAnimation(DynamicAnimation.ALPHA, endValue) {
                viewHolder.itemView.alpha = startValue
            }
        }
    }

    private inline fun addSpringAnimation(property: DynamicAnimation.ViewProperty, finalValue: Float = 0f, startBlock: () -> Unit) {
        val view = viewHolder.itemView
        startBlock()
        add(SpringAnimation(view, property, finalValue).apply {
            spring.stiffness = SpringForce.STIFFNESS_LOW
            spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        })
    }
}

class AppearanceAnimation(viewHolder: RecyclerView.ViewHolder, postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, onAnimationsEnd: (RecyclerView.ViewHolder) -> Unit) :
        ViewAnimation(viewHolder, onAnimationsEnd) {

    init {
        postLayoutInfo.let {
            val screenHeight = viewHolder.itemView.resources.displayMetrics.heightPixels
            translateY(screenHeight.toFloat())
        }
    }
}

class DisappearanceAnimation(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, onAnimationsEnd: (RecyclerView.ViewHolder) -> Unit) :
        ViewAnimation(viewHolder, onAnimationsEnd) {

    init {
        preLayoutInfo.let {
            val screenHeight = viewHolder.itemView.resources.displayMetrics.heightPixels
            translateY(0f, screenHeight.toFloat())
            alpha(1f, 0f)
        }
    }
}

class ChangeAnimation(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, onAnimationsEnd: (RecyclerView.ViewHolder) -> Unit) :
        ViewAnimation(viewHolder, onAnimationsEnd) {

    init {
        translateX((preLayoutInfo.centerX - postLayoutInfo.centerX).toFloat())
        translateY((preLayoutInfo.centerY - postLayoutInfo.centerY).toFloat())
        scaleX(getScale(preLayoutInfo.width, postLayoutInfo.width))
        scaleY(getScale(preLayoutInfo.height, postLayoutInfo.height))
    }

    private fun getScale(start: Int, end: Int) = start.toFloat() / end.toFloat()
}

class SampleItemAnimator : RecyclerView.ItemAnimator() {

    private val animations: HashMap<RecyclerView.ViewHolder, ViewAnimation> = HashMap()

    override fun isRunning() = !animations.isEmpty()

    private fun processAnimation(viewHolder: RecyclerView.ViewHolder, animation: ViewAnimation): Boolean {
        return if (animation.isEmpty) {
            dispatchAnimationFinished(viewHolder)
            false
        } else {
            animations[viewHolder] = animation
            true
        }
    }

    private fun finishAnimation(viewHolder: RecyclerView.ViewHolder) {
        animations.remove(viewHolder)
        with(viewHolder.itemView) {
            translationX = 0f
            translationY = 0f
            scaleX = 1f
            scaleY = 1f
            alpha = 1f
        }
        dispatchAnimationFinished(viewHolder)
    }

    override fun runPendingAnimations() {
        animations.forEach { it.value.start() }
    }

    override fun endAnimation(item: RecyclerView.ViewHolder?) {
        animations[item]?.skipToEnd()
    }

    override fun animateAppearance(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo?, postLayoutInfo: ItemHolderInfo): Boolean {
        return processAnimation(viewHolder, AppearanceAnimation(viewHolder, postLayoutInfo, this::finishAnimation))
    }

    override fun animateDisappearance(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo?): Boolean {
        return processAnimation(viewHolder, DisappearanceAnimation(viewHolder, preLayoutInfo, this::finishAnimation))
    }

    override fun animatePersistence(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo): Boolean {
        return processAnimation(viewHolder, ChangeAnimation(viewHolder, preLayoutInfo, postLayoutInfo, this::finishAnimation))
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo): Boolean {
        return processAnimation(newHolder, ChangeAnimation(newHolder, preLayoutInfo, postLayoutInfo, this::finishAnimation))
    }

    override fun endAnimations() {
        animations.forEach { it.value.skipToEnd() }
    }

}