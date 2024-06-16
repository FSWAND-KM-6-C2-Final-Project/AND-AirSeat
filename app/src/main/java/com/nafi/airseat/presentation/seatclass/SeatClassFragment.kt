package com.nafi.airseat.presentation.seatclass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.databinding.FragmentSeatClassBinding
import com.nafi.airseat.presentation.seatclass.adapter.SeatClassAdapter

class SeatClassFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSeatClassBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SeatClassAdapter

    val seatClassItems = listOf("Economy", "Premium Economy", "Business", "First Class") // List jenis-jenis kelas
    private var selectedSeatClass: String? = null
    private var listener: OnSeatClassSelectedListener? = null

    interface OnSeatClassSelectedListener {
        fun onSeatClassSelected(seatClass: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSeatClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupSeatClassAdapter()

        /*binding.btnSaveSeatClass.setOnClickListener {
            adapter.getSelectedSeatClass()?.let { selectedSeatClass ->
                Log.d("SeatClassFragment", "Selected seat class: $selectedSeatClass")
                listener?.onSeatClassSelected(selectedSeatClass)
            }
            dismiss()
        }*/
        binding.btnSaveSeatClass.setOnClickListener {
            selectedSeatClass?.let { seatClass ->
                listener?.onSeatClassSelected(seatClass) // Mengirimkan kelas kursi yang dipilih ke HomeFragment
            }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupSeatClassAdapter() {
        adapter =
            SeatClassAdapter(seatClassItems) { selectedSeatClass ->
                // listener?.onSeatClassSelected(selectedSeatClass)
                this.selectedSeatClass = selectedSeatClass
            }

        binding.rvSeatClass.layoutManager = LinearLayoutManager(context)
        binding.rvSeatClass.adapter = adapter
    }

    fun setOnSeatClassSelectedListener(listener: OnSeatClassSelectedListener) {
        this.listener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
