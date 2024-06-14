package com.nafi.airseat.presentation.searchticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.databinding.FragmentSearchTicketBinding
import com.nafi.airseat.presentation.common.sharedviewmodel.SharedViewModel
import com.nafi.airseat.presentation.searchticket.adapter.AirportsAdapter
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchTicketFragment(private val listener: ((Airport) -> Unit)? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSearchTicketBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel: SearchTicketViewModel by viewModel()
    private val airportAdapter: AirportsAdapter by lazy {
        AirportsAdapter {
            getClickedData(it)
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
        setAdapter()
        getAirportsData()
        handelSearchView()
    }

    private fun handelSearchView() {
        val searchView = binding.searchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    airportAdapter.filter.filter(newText)
                    return true
                }
            },
        )
    }

    private fun setAdapter() {
        binding.rvRecentSearches.adapter = this@SearchTicketFragment.airportAdapter
    }

    private fun getAirportsData() {
        viewModel.getAirportData().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                },
                doOnSuccess = {
                    result.payload?.let {
                        airportAdapter.submitData(it)
                    }
                },
                doOnEmpty = {
                    result.payload?.let {
                        airportAdapter.submitData(it)
                    }
                },
                doOnError = {
                },
            )
        }
    }

    private fun getClickedData(data: Airport) {
        // sharedViewModel.setAirportCity(data)
        listener?.invoke(data)
        dismiss()
    }
}
