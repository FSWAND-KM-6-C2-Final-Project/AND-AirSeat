package com.nafi.airseat.presentation.searchticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.databinding.FragmentSearchTicketBinding
import com.nafi.airseat.presentation.common.sharedviewmodel.SharedViewModel
import com.nafi.airseat.presentation.searchticket.adapter.AirportsAdapter
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchTicketFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSearchTicketBinding

    // private val binding get() = _binding!!
    //private var listener: OnAirportSelectedListener? = null
    //private lateinit var sharedViewModel: SharedViewModel
    private val viewModel: SearchTicketViewModel by viewModel()
    private val airportAdapter: AirportsAdapter by lazy {
        AirportsAdapter {
        }
    }

    /*interface OnAirportSelectedListener {
        fun onAirportSelected(airport: Airport)
    }*/

    private fun setupAirport() {
        binding.rvRecentSearches.apply {
            adapter = airportAdapter
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // val sharedViewModel: SharedViewModel by activityViewModels()
        // setupAirportRecyclerView()
        // setOnAirportSelectedListener()
        /*viewModel.airports.observe(viewLifecycleOwner) { airports ->
            airportAdapter.filterList(airports)
        }*/

        // airportAdapter.setAirportClickListener(this)

        /*val searchView = binding.searchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.filterAirports(newText)
                    return true
                }
            },
        )*/

        // airportAdapter.setAirportClickListener(this)
        // setAdapter()
        setupAirport()
        getAirportsData()

        // Set initial height of bottom sheet
        dialog?.setOnShowListener {
            val bottomSheet: View = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setAdapter() {
        binding.rvRecentSearches.adapter = this@SearchTicketFragment.airportAdapter
    }

    private fun getAirportsData() {
        viewModel.getAirportData().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.rvRecentSearches.visibility = View.GONE
                },
                doOnSuccess = {
                    binding.rvRecentSearches.visibility = View.VISIBLE
                    result.payload?.let { data ->
                        bindAirportList(data)
                        // Toast.makeText(requireContext(), "${it.size}", Toast.LENGTH_SHORT).show()
                    }
                },
            )
        }

        /*fun setOnAirportSelectedListener(listener: OnAirportSelectedListener) {
            this.listener = listener
        }*/

    /*override fun onResume() {
        super.onResume()
        viewModel.clearAirports()
    }*/

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAirportClicked(airport: Airport) {
        sharedViewModel.setDepartAirport(airport)
        Toast.makeText(requireContext(), "${airport.airportName}", Toast.LENGTH_SHORT).show()
        dismiss() // Tutup bottom sheet setelah bandara dipilih
    }

    private fun setupAirportRecyclerView() {
        airportAdapter =
            AirportAdapter(emptyList(), this) */
    /*{ selectedAirport ->
                val homeFragment = parentFragment as? HomeFragment
                homeFragment?.onAirportSelected(selectedAirport)
                dismiss()
            }*/
    /*

        binding.rvRecentSearches.apply {
            adapter = airportAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }*/
    }

    private fun bindAirportList(airports: List<Airport>) {
        airportAdapter.submitData(airports)
    }
}
