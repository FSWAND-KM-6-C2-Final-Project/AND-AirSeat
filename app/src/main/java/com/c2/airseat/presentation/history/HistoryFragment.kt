package com.c2.airseat.presentation.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.c2.airseat.R
import com.c2.airseat.data.model.History
import com.c2.airseat.databinding.FragmentHistoryBinding
import com.c2.airseat.presentation.calendar.CalendarBottomSheetFragment
import com.c2.airseat.presentation.calendar.CalendarHistoryBottomSheetFragment
import com.c2.airseat.presentation.common.views.ContentState
import com.c2.airseat.presentation.detailhistory.DetailHistoryActivity
import com.c2.airseat.presentation.history.adapter.HistoryDataItem
import com.c2.airseat.presentation.history.adapter.MonthHeaderItem
import com.c2.airseat.presentation.login.LoginActivity
import com.c2.airseat.presentation.searcthistory.SearchHistoryFragment
import com.c2.airseat.utils.ApiErrorException
import com.c2.airseat.utils.NoInternetException
import com.c2.airseat.utils.proceedWhen
import com.c2.airseat.utils.toMonthYearFormat
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class HistoryFragment : Fragment(), CalendarBottomSheetFragment.OnDateSelectedListener {
    private val viewModel: HistoryViewModel by viewModel()
    private lateinit var binding: FragmentHistoryBinding
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private var listenBookingCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        observeHistoryData()
        setOnClickListener()
    }

    override fun onDateSelected(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ) {
    }

    private fun setUpAdapter() {
        binding.rvHistory.adapter = this@HistoryFragment.groupAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setOnClickListener() {
        binding.ivSearchHistory.setOnClickListener {
            val dialog = SearchHistoryFragment()
            dialog.show(childFragmentManager, dialog.tag)
            childFragmentManager.setFragmentResultListener(
                "bookingCode",
                this,
            ) { _, bundle ->
                listenBookingCode = bundle.getString("data")
                observeSearchData(listenBookingCode)
            }
        }

        binding.ivClearHistory.setOnClickListener {
            observeHistoryData()
        }

        binding.ivClearWhiteHistory.setOnClickListener {
            observeHistoryData()
        }

        binding.layoutLoginProtectionHistory.btnLogin.setOnClickListener {
            navigateToLogin()
        }

        binding.btnFilterDateHistory.setOnClickListener {
            val bottomSheet = CalendarHistoryBottomSheetFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
            childFragmentManager.setFragmentResultListener(
                "rangeDate",
                this,
            ) { _, bundle ->
                val startDate = bundle.getString("startDate")
                val endDate = bundle.getString("endDate")
                observeSearchDataByDate(startDate = startDate, endDate = endDate)
            }
        }

        binding.btnClearFilterDateHistory.setOnClickListener {
            observeHistoryData()
        }
    }

    private fun observeHistoryData() {
        viewModel.getHistoryData(bookingCode = null, startDate = null, endDate = null).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    binding.layoutHistory.isVisible = true
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.LOADING)
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = false
                    binding.btnFilterDateHistory.isVisible = false
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    binding.btnFilterDateHistory.isVisible = false
                    binding.btnClearFilterDateHistory.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    binding.layoutHistory.isVisible = true
                    binding.bgHistoryGradient.isVisible = true
                    binding.rvHistory.isVisible = true
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.SUCCESS)
                    binding.ivSearchHistory.isVisible = true
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = false
                    binding.btnFilterDateHistory.isVisible = true
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    binding.btnFilterDateHistory.isVisible = true
                    binding.btnClearFilterDateHistory.isVisible = false
                    binding.ivSearchHistory.drawable.setTint(resources.getColor(R.color.white))
                    result.payload?.let { data ->
                        val items = mutableListOf<BindableItem<*>>()

                        val groupedData = data.groupBy { it.createdAt.toMonthYearFormat() }

                        groupedData.forEach { (monthYear, dataList) ->
                            items.add(MonthHeaderItem(monthYear))
                            dataList.forEach { data ->
                                items.add(
                                    HistoryDataItem(data) { history ->
                                        navigateToDetailHistory(history)
                                    },
                                )
                            }
                        }
                        groupAdapter.update(items)
                    }
                },
                doOnEmpty = {
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    binding.layoutHistory.isVisible = true
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.EMPTY)
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = false
                    binding.btnFilterDateHistory.isVisible = true
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    binding.btnFilterDateHistory.isVisible = true
                    binding.btnClearFilterDateHistory.isVisible = false
                },
                doOnError = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = false
                    binding.layoutHistory.isVisible = false
                    binding.btnFilterDateHistory.isVisible = false
                    binding.btnFilterDateHistory.isVisible = true
                    binding.btnClearFilterDateHistory.isVisible = false
                    if (it.exception is ApiErrorException) {
                        val errorBody = it.exception.errorResponse.message
                        if (errorBody == "jwt malformed" || errorBody == "Token not found!") {
                            binding.layoutLoginProtectionHistory.root.isVisible = true
                        } else {
                            binding.csvHistory.setState(ContentState.ERROR_GENERAL)
                        }
                    } else if (it.exception is NoInternetException) {
                        binding.csvHistory.setState(ContentState.ERROR_NETWORK)
                    }
                },
            )
        }
    }

    private fun observeSearchData(bookingCode: String?) {
        viewModel.getHistoryData(bookingCode, startDate = null, endDate = null).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.LOADING)
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = false
                },
                doOnSuccess = {
                    binding.bgHistoryGradient.isVisible = true
                    binding.rvHistory.isVisible = true
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.SUCCESS)
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = true
                    result.payload?.let { data ->
                        val items = mutableListOf<BindableItem<*>>()

                        val groupedData = data.groupBy { it.createdAt.toMonthYearFormat() }

                        groupedData.forEach { (monthYear, dataList) ->
                            items.add(MonthHeaderItem(monthYear))
                            dataList.forEach { data ->
                                items.add(
                                    HistoryDataItem(data) { history ->
                                        navigateToDetailHistory(history)
                                    },
                                )
                            }
                        }
                        groupAdapter.update(items)
                    }
                },
                doOnEmpty = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.EMPTY)
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = true
                    binding.ivClearWhiteHistory.isVisible = false
                },
                doOnError = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.ivClearWhiteHistory.isVisible = false
                    if (it.exception is ApiErrorException) {
                        val errorBody = it.exception.errorResponse.message
                        if (errorBody == "jwt malformed" || errorBody == "Token not found!") {
                            binding.layoutLoginProtectionHistory.root.isVisible = true
                        } else {
                            binding.csvHistory.setState(ContentState.ERROR_GENERAL)
                        }
                    } else if (it.exception is NoInternetException) {
                        binding.csvHistory.setState(ContentState.ERROR_NETWORK)
                    }
                },
            )
        }
    }

    private fun observeSearchDataByDate(
        startDate: String?,
        endDate: String?,
    ) {
        viewModel.getHistoryData(bookingCode = null, startDate, endDate).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.LOADING)
                    binding.ivSearchHistory.isVisible = false
                    binding.ivSearchHistory.drawable.setTint(resources.getColor(R.color.white))
                    binding.btnFilterDateHistory.isVisible = false
                    binding.btnClearFilterDateHistory.isVisible = false
                },
                doOnSuccess = {
                    binding.bgHistoryGradient.isVisible = true
                    binding.rvHistory.isVisible = true
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.SUCCESS)

                    result.payload?.let { data ->
                        val items = mutableListOf<BindableItem<*>>()

                        val groupedData = data.groupBy { it.createdAt.toMonthYearFormat() }

                        groupedData.forEach { (monthYear, dataList) ->
                            items.add(MonthHeaderItem(monthYear))
                            dataList.forEach { data ->
                                items.add(
                                    HistoryDataItem(data) { history ->
                                        navigateToDetailHistory(history)
                                    },
                                )
                            }
                        }
                        groupAdapter.update(items)
                    }
                    binding.ivSearchHistory.isVisible = true
                    binding.ivSearchHistory.drawable.setTint(resources.getColor(R.color.white))
                    binding.btnFilterDateHistory.isVisible = false
                    binding.btnClearFilterDateHistory.isVisible = true
                },
                doOnEmpty = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.csvHistory.setState(ContentState.EMPTY)
                    binding.ivSearchHistory.isVisible = true
                    binding.ivSearchHistory.drawable.setTint(resources.getColor(R.color.black))
                    binding.btnFilterDateHistory.isVisible = false
                    binding.btnClearFilterDateHistory.isVisible = true
                },
                doOnError = {
                    binding.bgHistoryGradient.isVisible = false
                    binding.rvHistory.isVisible = false
                    binding.tvTitleHistory.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black,
                        ),
                    )
                    binding.ivSearchHistory.isVisible = false
                    binding.ivClearHistory.isVisible = false
                    binding.btnClearFilterDateHistory.isVisible = false
                    if (it.exception is ApiErrorException) {
                        val errorBody = it.exception.errorResponse.message
                        if (errorBody == "jwt malformed" || errorBody == "Token not found!") {
                            binding.layoutLoginProtectionHistory.root.isVisible = true
                        } else {
                            binding.csvHistory.setState(ContentState.ERROR_GENERAL)
                        }
                    } else if (it.exception is NoInternetException) {
                        binding.csvHistory.setState(ContentState.ERROR_NETWORK)
                    }
                },
            )
        }
    }

    private fun navigateToDetailHistory(data: History) {
        DetailHistoryActivity.startActivity(requireContext(), data)
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
