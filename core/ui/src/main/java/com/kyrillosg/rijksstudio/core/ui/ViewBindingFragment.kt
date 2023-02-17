package com.kyrillosg.rijksstudio.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias BindingProvider<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class ViewBindingFragment<T : ViewBinding>(
    private val bindingProvider: BindingProvider<T>,
) : Fragment() {

    private var _binding: T? = null

    val binding: T
        get() = _binding ?: error("This property is only valid between onCreateView and onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingProvider(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
