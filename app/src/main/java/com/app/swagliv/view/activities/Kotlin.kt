package com.example.flyinghearts

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.app.swagliv.databinding.ActivityChatBinding
import kotlin.random.Random


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: ActivityChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityChatBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.heart.setOnClickListener {
            heartOnClick()
        }
    }

    private fun heartOnClick() {
        // Disable clips on all parent generations
        disableAllParentsClip(binding.heart)

        // Create clone
        val imageClone = cloneImage()

        // Animate
        animateFlying(imageClone)
        animateFading(imageClone)
    }

    private fun cloneImage(): ImageView {
        val clone = ImageView(context)
        clone.layoutParams = binding.heart.layoutParams
        clone.setImageDrawable(binding.heart.drawable)
        binding.cloneContainer.addView(clone)
        return clone
    }

    private fun animateFlying(image: ImageView) {
        val x = 0f
        val y = 0f
        val r = Random.nextInt(1000, 5000)
        val angle = 25f

        val path = Path().apply {
            when (r % 2) {
                0 -> arcTo(RectF(x, y - r, x + 2 * r, y + r), 180f, angle)
                else -> arcTo(RectF(x - 2 * r, y - r, x, y + r), 0f, -angle)
            }
        }

        ObjectAnimator.ofFloat(image, View.X, View.Y, path).apply {
            duration = 1000
            start()
        }
    }

    private fun animateFading(image: ImageView) {
        image.animate()
            .alpha(0f)
            .setDuration(1000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.cloneContainer.removeView(image)
                }
            })
    }

    private fun disableAllParentsClip(view: View) {
        var view = view
        view.parent?.let {
            while (view.parent is ViewGroup) {
                val viewGroup = view.parent as ViewGroup
                viewGroup.clipChildren = false
                viewGroup.clipToPadding = false
                view = viewGroup
            }
        }
    }
}
