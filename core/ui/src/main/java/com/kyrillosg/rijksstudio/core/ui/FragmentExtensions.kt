package com.kyrillosg.rijksstudio.core.ui

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}
