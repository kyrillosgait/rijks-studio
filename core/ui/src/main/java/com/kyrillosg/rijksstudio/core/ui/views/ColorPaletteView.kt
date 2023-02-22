package com.kyrillosg.rijksstudio.core.ui.views

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.kyrillosg.rijksstudio.core.ui.databinding.ViewColorPaletteBinding

class ColorPaletteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val binding = ViewColorPaletteBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (!isInEditMode) {
            binding.root.isVisible = false
        }
    }

    fun init(model: Model) {
        binding.root.isVisible = model.colors.isNotEmpty()

        binding.header.text = model.label
        binding.header.isVisible = model.label.isNotEmpty()

        val views = listOf(
            binding.firstColor,
            binding.secondColor,
            binding.thirdColor,
            binding.fourthColor,
            binding.fifthColor,
            binding.sixthColor,
        )

        views.forEach {
            it.visibility = View.INVISIBLE
        }

        model.colors.take(6).forEachIndexed { index, color ->
            with(views[index]) {
                background = ColorDrawable(color.colorInt)
                text = color.percentage
                visibility = View.VISIBLE
            }
        }
    }

    data class Model(
        val label: String,
        val colors: List<Color>,
    ) {
        data class Color(
            @ColorInt val colorInt: Int,
            val percentage: String,
        )
    }
}
