package com.nafi.airseat.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nafi.airseat.databinding.FragmentHistoryBinding
import com.nafi.airseat.presentation.bottomsheet.ProtectedLoginBottomSheet

class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivSearchHistory.setOnClickListener {
            // showDateRangePicker()
            val dialog = ProtectedLoginBottomSheet()
            dialog.show(childFragmentManager, dialog.tag)
        }
    }
}
