package com.c2.airseat.presentation.searcthistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import com.c2.airseat.data.model.SearchHistory
import com.c2.airseat.databinding.FragmentSearchHistoryBinding
import com.c2.airseat.presentation.searcthistory.adapter.SearchHistoryAdapter
import com.c2.airseat.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchHistoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSearchHistoryBinding
    private val viewModel: SearchHistoryViewModel by viewModel()
    private val adapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter {
            viewModel.removeSearchHistory(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setHeightBottomSheet()
        setAdapter()
        observeData()
        handelSearchView()
        setOnclickListener()
    }

    private fun setHeightBottomSheet() {
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        val height = (screenHeight * 0.87).toInt()
        bottomSheet.layoutParams.height = height

        bottomSheet.layoutParams = bottomSheet.layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setOnclickListener() {
        binding.tvDeleteAllHistory.setOnClickListener {
            viewModel.deleteAll()
            observeData()
        }
    }

    private fun setAdapter() {
        binding.rvRecentSearches.adapter = this@SearchHistoryFragment.adapter
    }

    private fun handelSearchView() {
        val searchView = binding.svHistory
        searchView.isIconified = false
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val searchHistory = SearchHistory(searchHistory = query)
                    addSearchHistory(searchHistory)
                    val result =
                        Bundle().apply {
                            putString("data", query)
                        }
                    parentFragmentManager.setFragmentResult("bookingCode", result)
                    dismiss()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            },
        )
    }

    private fun observeData() {
        viewModel.getAllSearchHistory().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                },
                doOnSuccess = {
                    result.payload?.let {
                        adapter.insertData(it)
                    }
                },
                doOnEmpty = {
                    result.payload?.let {
                        adapter.insertData(it)
                    }
                },
                doOnError = {
                },
            )
        }
    }

    private fun addSearchHistory(searchHistory: SearchHistory) {
        viewModel.createSearchHistory(searchHistory).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                },
                doOnSuccess = {
                },
                doOnEmpty = {
                },
                doOnError = {
                },
            )
        }
    }
}
