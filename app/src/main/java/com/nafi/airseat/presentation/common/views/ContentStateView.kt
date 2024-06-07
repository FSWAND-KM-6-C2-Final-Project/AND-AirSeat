package com.nafi.airseat.presentation.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.LayoutContentStateBinding

class ContentStateView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: LayoutContentStateBinding

    private var listener: ContentStateListener? = null

    init {
        inflate(context, R.layout.layout_content_state, this)
        binding = LayoutContentStateBinding.bind(this)
    }

    fun setListener(listener: ContentStateListener) {
        this.listener = listener
    }

    fun setState(
        state: ContentState,
        title: String? = null,
        desc: String? = null,
        @DrawableRes imgRes: Int? = null,
    ) {
        when (state) {
            ContentState.SUCCESS -> {
                binding.root.isVisible = false
                binding.layoutEmptyData.isVisible = false
                binding.layoutLoginProtection.isVisible = false
                binding.layoutErrorGeneral.isVisible = false
                binding.layoutLoadingData.isVisible = false
                binding.layoutNoConnection.isVisible = false
                listener?.onContentShow(true)
            }

            ContentState.LOADING -> {
                binding.root.isVisible = true
                binding.layoutEmptyData.isVisible = false
                binding.layoutLoginProtection.isVisible = false
                binding.layoutErrorGeneral.isVisible = false
                binding.layoutLoadingData.isVisible = true
                binding.layoutNoConnection.isVisible = false
                listener?.onContentShow(false)
                title?.let { binding.tvTitleLoadingData.text = it } ?: run {
                    binding.tvTitleLoadingData.text = context.getString(R.string.text_title_loading_data)
                }
            }

            ContentState.EMPTY -> {
                binding.root.isVisible = true
                binding.layoutEmptyData.isVisible = true
                binding.layoutLoginProtection.isVisible = false
                binding.layoutErrorGeneral.isVisible = false
                binding.layoutLoadingData.isVisible = false
                binding.layoutNoConnection.isVisible = false
                title?.let { binding.tvTitleEmptyData.text = it } ?: run {
                    binding.tvTitleEmptyData.text = context.getString(R.string.text_title_empty_data)
                }

                desc?.let { binding.tvTitleEmptyData.text = it } ?: run {
                    binding.tvTitleEmptyData.text = context.getString(R.string.text_desc_empty_data)
                }
                listener?.onContentShow(false)
            }

            ContentState.ERROR_NETWORK -> {
                binding.root.isVisible = true
                binding.layoutEmptyData.isVisible = false
                binding.layoutLoginProtection.isVisible = false
                binding.layoutErrorGeneral.isVisible = false
                binding.layoutLoadingData.isVisible = false
                binding.layoutNoConnection.isVisible = true
                listener?.onContentShow(false)
            }

            ContentState.ERROR_GENERAL -> {
                binding.root.isVisible = true
                binding.layoutEmptyData.isVisible = false
                binding.layoutLoginProtection.isVisible = false
                binding.layoutErrorGeneral.isVisible = true
                binding.layoutLoadingData.isVisible = false
                binding.layoutNoConnection.isVisible = false
                listener?.onContentShow(false)
            }
        }
    }
}

interface ContentStateListener {
    fun onContentShow(isContentShow: Boolean)
}

enum class ContentState {
    SUCCESS,
    LOADING,
    EMPTY,
    ERROR_NETWORK,
    ERROR_GENERAL,
}