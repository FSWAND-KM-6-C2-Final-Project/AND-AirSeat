package com.c2.airseat.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.c2.airseat.databinding.FragmentFilterBottomSheetBinding
import com.c2.airseat.presentation.filter.adapter.FilterAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FilterAdapter
    private val viewModel: FilterViewModel by viewModels()
    private var selectedFilter: String? = null
    private var listener: OnFilterSelectedListener? = null

    interface OnFilterSelectedListener {
        fun onFilterSelected(filterSelected: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupFilterAdapter()

        binding.btnSaveFilter.setOnClickListener {
            selectedFilter?.let { filterSelected ->
                listener?.onFilterSelected(filterSelected)
            }
            dismiss()
        }

        viewModel.filterItems.observe(
            viewLifecycleOwner,
            Observer { filterItems ->
                adapter.updateItems(filterItems)
            },
        )
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupFilterAdapter() {
        adapter =
            FilterAdapter(emptyList()) { selectedFilter ->
                this.selectedFilter = selectedFilter
            }

        binding.rvFilter.layoutManager = LinearLayoutManager(context)
        binding.rvFilter.adapter = adapter
    }

    fun setOnFilterSelectedListener(listener: OnFilterSelectedListener) {
        this.listener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
