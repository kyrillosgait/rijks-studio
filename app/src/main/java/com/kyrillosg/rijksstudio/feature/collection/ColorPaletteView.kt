package com.kyrillosg.rijksstudio.feature.collection

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.kyrillosg.rijksstudio.core.model.CollectionItemColor
import com.kyrillosg.rijksstudio.databinding.ViewColorPaletteBinding

class ColorPaletteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val binding = ViewColorPaletteBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.root.isVisible = false
    }

    fun init(colorModels: List<ColorModel>) {
        if (colorModels.isNotEmpty()) binding.root.isVisible = true

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

        colorModels.take(6).forEachIndexed { index, model ->
            with(views[index]) {
                background = ColorDrawable(model.color)
                text = model.percentage
                visibility = View.VISIBLE
            }
        }
    }

    data class ColorModel(
        @ColorInt val color: Int,
        val percentage: String,
    ) {

        companion object {
            fun from(collectionItemColor: CollectionItemColor): ColorModel {
                return ColorModel(
                    color = Color.parseColor(collectionItemColor.hex.trim()),
                    percentage = "${collectionItemColor.percentage}%",
                )
            }
        }
    }
}