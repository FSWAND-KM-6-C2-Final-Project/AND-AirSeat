package com.c2.airseat.presentation.searchticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.c2.airseat.data.model.Airport
import com.c2.airseat.data.model.AirportHistory
import com.c2.airseat.databinding.FragmentSearchTicketBinding
import com.c2.airseat.presentation.searchticket.adapter.AirportHistoryAdapter
import com.c2.airseat.presentation.searchticket.adapter.AirportsAdapter
import com.c2.airseat.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchTicketFragment(private val listener: ((Airport) -> Unit)? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSearchTicketBinding
    private val viewModel: SearchTicketViewModel by viewModel()
    private val airportAdapter: AirportsAdapter by lazy {
        AirportsAdapter {
            getClickedData(it)
        }
    }
    private val adapter: AirportHistoryAdapter by lazy {
        AirportHistoryAdapter {
            viewModel.removeAirportHistory(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        setHeightBottomSheet()
        setAdapter()
        observeData()
        handelSearchView()
        deleteAllHistories()
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

    private fun handelSearchView() {
        val searchView = binding.searchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.rvRecentSearches.isVisible = true
                    addAirportHistory(AirportHistory(airportHistory = query))
                    getAirportsData(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        binding.rvRecentSearches.isVisible = false
                        observeData()
                    }
                    return true
                }
            },
        )
    }

    private fun setAdapter() {
        binding.rvRecentSearches.adapter = this@SearchTicketFragment.airportAdapter
        binding.rvResultSearches.adapter = this@SearchTicketFragment.adapter
    }

    private fun getAirportsData(cityName: String?) {
        viewModel.getAirportData(cityName).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.rvRecentSearches.isVisible = false
                    binding.rvResultSearches.isVisible = false
                },
                doOnSuccess = {
                    binding.rvRecentSearches.isVisible = true
                    binding.rvResultSearches.isVisible = false
                    result.payload?.let {
                        airportAdapter.submitData(it)
                    }
                },
                doOnEmpty = {
                    result.payload?.let {
                        airportAdapter.submitData(it)
                    }
                    binding.rvRecentSearches.isVisible = false
                    binding.rvResultSearches.isVisible = true
                },
                doOnError = {
                    binding.rvRecentSearches.isVisible = false
                    binding.rvResultSearches.isVisible = true
                },
            )
        }
    }

    private fun getClickedData(data: Airport) {
        // sharedViewModel.setAirportCity(data)
        listener?.invoke(data)
        dismiss()
    }

    private fun observeData() {
        viewModel.getAllAirportHistory().observe(viewLifecycleOwner) { result ->
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

    private fun addAirportHistory(airportHistory: AirportHistory) {
        viewModel.createAirportHistory(airportHistory).observe(viewLifecycleOwner) { result ->
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

    private fun deleteAllHistories() {
        binding.tvDeleteHistorySearches.setOnClickListener {
            viewModel.deleteAllAirport()
        }
    }
}
