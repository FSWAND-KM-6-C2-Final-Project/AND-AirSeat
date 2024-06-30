package com.c2.airseat.presentation.seatbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.c2.airseat.R
import com.c2.airseat.data.source.network.model.booking.BookingPassenger
import com.c2.airseat.data.source.network.model.booking.SeatPassenger
import com.c2.airseat.databinding.ActivitySeatBookBinding
import com.c2.airseat.presentation.common.views.ContentState
import com.c2.airseat.presentation.flightdetail.FlightDetailPriceActivity
import com.c2.airseat.presentation.seatbookreturn.SeatBookReturnActivity
import com.c2.airseat.utils.proceedWhen
import com.c2.airseat.utils.seatbook.SeatBookView
import com.c2.airseat.utils.showSnackBarError
import dev.jahidhasanco.seatbookview.SeatClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeatBookActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView

    private val seatViewModel: SeatViewModel by viewModel()

    private val binding: ActivitySeatBookBinding by lazy {
        ActivitySeatBookBinding.inflate(layoutInflater)
    }

    private lateinit var passengerList: MutableList<BookingPassenger>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        seatBookView = findViewById(R.id.layout_seat)

        val flightId = intent.getStringExtra("flightId")
        val idReturn = intent.getIntExtra("idReturn", 0)
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)
        val price = intent.getIntExtra("price", 0)
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()

        val totalPassengers = adultCount + childCount
        if (flightId != null && seatClassChoose != null) {
            getSeatData(flightId, seatClassChoose)
        }

        seatBookView.setSelectSeatLimit(totalPassengers)

        binding.tvHeaderDestinationInfo.text =
            getString(
                R.string.text_destination_info,
                airportCityCodeDeparture,
                airportCityCodeDestination,
                seatClassChoose,
            )
        binding.layoutSeatbook.typeSeat.text = getString(R.string.text_type_seat, seatClassChoose)

        binding.btnSave.setOnClickListener {
            if (seatBookView.getSelectedSeatCount() == totalPassengers) {
                val nextActivityClass =
                    if (idReturn != 0) {
                        SeatBookReturnActivity::class.java
                    } else {
                        FlightDetailPriceActivity::class.java
                    }

                val intent =
                    Intent(this, nextActivityClass).apply {
                        putExtra("adults", adultCount)
                        putExtra("child", childCount)
                        putExtra("baby", babyCount)
                        putExtra("totalPassenger", totalPassengers)
                        putExtra("price", price)
                        putExtra("full_name", intent.getStringExtra("full_name"))
                        putExtra("number_phone", intent.getStringExtra("number_phone"))
                        putExtra("email", intent.getStringExtra("email"))
                        putExtra("family_name", intent.getStringExtra("family_name"))
                        putExtra("airportCityCodeDeparture", airportCityCodeDeparture)
                        putExtra("airportCityCodeDestination", airportCityCodeDestination)
                        putExtra("seatClassChoose", seatClassChoose)
                        putExtra("idDepart", flightId)
                        putExtra("tax", 300000.0)
                        putExtra("promo", 0.0)
                        putExtra("passenger_list", ArrayList(passengerList))
                        putExtra("idReturn", idReturn)
                    }
                startActivity(intent)
            } else {
                showSnackBarError(getString(R.string.text_please_select_seat))
            }
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getSeatData(
        flightId: String,
        seatClassChoose: String,
    ) {
        seatViewModel.getFormattedSeatData(flightId, seatClassChoose).observe(
            this,
        ) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.csvSeat.setState(ContentState.LOADING)
                    binding.svSeatbook.isVisible = false
                    binding.cvBtnSave.isVisible = false
                },
                doOnSuccess = {
                    binding.csvSeat.setState(ContentState.SUCCESS)
                    binding.svSeatbook.isVisible = true
                    binding.cvBtnSave.isVisible = true
                    val formattedSeatStatus = it.payload?.first.orEmpty()
                    val formattedSeatNames = it.payload?.second.orEmpty()
                    val seatName = it.payload?.third.orEmpty()
                    showSeatBookView(formattedSeatStatus, formattedSeatNames)
                    setClickListenerSeat(seatName)
                },
                doOnError = {
                    binding.csvSeat.setState(ContentState.ERROR_GENERAL)
                    binding.svSeatbook.isVisible = false
                    binding.cvBtnSave.isVisible = false
                },
                doOnEmpty = {
                    binding.csvSeat.setState(ContentState.EMPTY)
                    binding.svSeatbook.isVisible = false
                    binding.cvBtnSave.isVisible = false
                },
            )
        }
    }

    private fun showSeatBookView(
        formattedSeatStatus: String,
        formattedSeatNames: List<String>,
    ) {
        seatBookView.setSeatsLayoutString(formattedSeatStatus)
            .isCustomTitle(true)
            .setCustomTitle(formattedSeatNames)
            .setSeatLayoutPadding(2)
            .setSeatSizeBySeatsColumnAndLayoutWidth(6, -1)

        seatBookView.show()
    }

    private fun setClickListenerSeat(seatNamesList: List<String>) {
        val adult = passengerList.indices.filter { passengerList[it].passengerType == "adult" }
        val baby = passengerList.indices.filter { passengerList[it].passengerType == "infant" }

        seatBookView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    selectedIdList.forEachIndexed { index, selectedSeatId ->
                        if (index < passengerList.size) {
                            val seatName = seatNamesList[selectedSeatId - 1]
                            val row = seatName.last().toString()
                            val column = seatName.dropLast(1)
                            val seatPassenger = SeatPassenger(seatRow = row, seatColumn = column)

                            passengerList[index].seatDeparture = seatPassenger

                            if (index in adult) {
                                val adultIndex = adult.indexOf(index)
                                if (adultIndex < baby.size) {
                                    val babyIndex = baby[adultIndex]
                                    passengerList[babyIndex].seatDeparture = seatPassenger
                                }
                            }
                        }
                    }
                }

                override fun onBookedSeatClick(view: View) {
                }

                override fun onReservedSeatClick(view: View) {
                }
            },
        )
    }
}
