package com.nafi.airseat.presentation.seatclass

import SeatClassAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.data.model.SeatClass
import com.nafi.airseat.databinding.FragmentSeatClassBinding

class SeatClassFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSeatClassBinding
    private lateinit var adapter: SeatClassAdapter // Create adapter variable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSeatClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize adapter
        adapter =
            SeatClassAdapter { seatclass ->
                Toast.makeText(requireContext(), seatclass.name, Toast.LENGTH_SHORT).show()
            }

        // Set layout manager and adapter to RecyclerView
        binding.rvDate.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDate.adapter = adapter

        // Add sample data (replace with your actual data)
        val seatClassList =
            listOf(
                SeatClass(1, "Economy"),
                SeatClass(2, "Business"),
                SeatClass(3, "First Class"),
            )
        adapter.submitList(seatClassList)

        binding.btnSaveSeatClass.visibility = View.VISIBLE
        binding.btnSaveSeatClass.setOnClickListener {
            // Your save logic here
            dismiss() // Optionally dismiss the bottom sheet after save
        }
    }
}
