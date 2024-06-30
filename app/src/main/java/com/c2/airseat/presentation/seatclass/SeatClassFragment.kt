package com.c2.airseat.presentation.seatclass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.c2.airseat.databinding.FragmentSeatClassBinding
import com.c2.airseat.presentation.seatclass.adapter.SeatClassAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SeatClassFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSeatClassBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SeatClassAdapter
    private val viewModel: SeatClassViewModel by viewModels()
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

        binding.btnSaveSeatClass.setOnClickListener {
            selectedSeatClass?.let { seatClass ->
                listener?.onSeatClassSelected(seatClass)
            }
            dismiss()
        }

        viewModel.seatClassItems.observe(
            viewLifecycleOwner,
            Observer { seatClassItems ->
                adapter.updateItems(seatClassItems)
            },
        )
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupSeatClassAdapter() {
        adapter =
            SeatClassAdapter(emptyList()) { selectedSeatClass ->
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
