package com.nafi.airseat.presentation.passengers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.databinding.FragmentPassengersBinding

class PassengersFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPassengersBinding
    // private val viewModel: PassengersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPassengersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            // Your save logic here
            dismiss() // Optionally dismiss the bottom sheet after save
        }
    }
}
