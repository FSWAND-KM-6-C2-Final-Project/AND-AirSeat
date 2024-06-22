package com.nafi.airseat.presentation.passengers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.databinding.FragmentPassengersBinding

class PassengersFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPassengersBinding
    private val viewModel: PassengersViewModel by viewModels({ requireParentFragment() })
    private var listener: OnPassengerCountUpdatedListener? = null

    interface OnPassengerCountUpdatedListener {
        fun onPassengerCountUpdated(
            count: Int,
            adultCount: Int,
            childCount: Int,
            babyCount: Int,
        )
    }

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

        // Initialize the text views with default counts
        binding.adultCount.text = viewModel.adultCount.toString()
        binding.childCount.text = viewModel.childCount.toString()
        binding.babyCount.text = viewModel.babyCount.toString()

        // Increment or decrement counts when buttons are clicked
        binding.plusAdult.setOnClickListener {
            viewModel.incrementAdultCount()
            updatePassengerCount()
        }

        binding.minusAdult.setOnClickListener {
            viewModel.decrementAdultCount()
            updatePassengerCount()
        }

        binding.plusChild.setOnClickListener {
            viewModel.incrementChildCount()
            updatePassengerCount()
        }

        binding.minusChild.setOnClickListener {
            viewModel.decrementChildCount()
            updatePassengerCount()
        }

        binding.plusBaby.setOnClickListener {
            viewModel.incrementBabyCount()
            updatePassengerCount()
        }

        binding.minusBaby.setOnClickListener {
            viewModel.decrementBabyCount()
            updatePassengerCount()
        }

        // Save button click listener
        binding.btnSave.setOnClickListener {
            // Update the passenger count in HomeFragment
            listener?.onPassengerCountUpdated(
                viewModel.getTotalPassengerCount(),
                viewModel.adultCount,
                viewModel.childCount,
                viewModel.babyCount,
            )

            // Dismiss the bottom sheet
            dismiss()
        }
    }

    private fun updatePassengerCount() {
        binding.adultCount.text = viewModel.adultCount.toString()
        binding.childCount.text = viewModel.childCount.toString()
        binding.babyCount.text = viewModel.babyCount.toString()
    }

    fun setOnPassengerCountUpdatedListener(listener: OnPassengerCountUpdatedListener) {
        this.listener = listener
    }
}
