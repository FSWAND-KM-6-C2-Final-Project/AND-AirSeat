package com.nafi.airseat.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafi.airseat.R
import com.nafi.airseat.databinding.FragmentHistoryBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.history.adapter.HistoryDataItem
import com.nafi.airseat.presentation.history.adapter.MonthHeaderItem
import com.nafi.airseat.presentation.searcthistory.SearchHistoryFragment
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.toMonthYearFormat
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
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
    }

    private fun observeHistoryData() {
        viewModel.getHistoryData(bookingCode = null).observe(viewLifecycleOwner) { result ->
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
                    binding.layoutLoginProtectionHistory.root.isVisible = false
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
                    binding.layoutLoginProtectionHistory.root.isVisible = false
                    result.payload?.let { data ->
                        val items = mutableListOf<BindableItem<*>>()

                        val groupedData = data.groupBy { it.createdAt.toMonthYearFormat() }

                        groupedData.forEach { (monthYear, dataList) ->
                            items.add(MonthHeaderItem(monthYear))
                            dataList.forEach { data ->
                                items.add(HistoryDataItem(data))
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
                    binding.layoutLoginProtectionHistory.root.isVisible = false
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
        viewModel.getHistoryData(bookingCode).observe(viewLifecycleOwner) { result ->
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
                                items.add(HistoryDataItem(data))
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
                    if (it.exception is NoInternetException) {
                        binding.csvHistory.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvHistory.setState(
                            ContentState.ERROR_GENERAL,
                            desc = result.exception?.message.orEmpty(),
                        )
                    }
                },
            )
        }
    }
}
