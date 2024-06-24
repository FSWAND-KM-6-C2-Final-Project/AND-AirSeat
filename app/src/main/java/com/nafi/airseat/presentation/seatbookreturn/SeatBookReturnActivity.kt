package com.nafi.airseat.presentation.seatbookreturn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.data.source.network.model.booking.BookingPassenger
import com.nafi.airseat.data.source.network.model.booking.SeatPassenger
import com.nafi.airseat.databinding.ActivitySeatBookReturnBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.flightdetail.FlightDetailPriceActivity
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.seatbookreturn.SeatBookReturnView
import dev.jahidhasanco.seatbookview.SeatClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeatBookReturnActivity : AppCompatActivity() {
    private lateinit var seatBookReturnView: SeatBookReturnView

    private val seatViewModel: SeatReturnViewModel by viewModel()

    private val binding: ActivitySeatBookReturnBinding by lazy {
        ActivitySeatBookReturnBinding.inflate(layoutInflater)
    }

    private lateinit var passengerList: MutableList<BookingPassenger>
    // private lateinit var seats: List<Seat>
    // private lateinit var seatNames: List<String>
    // private lateinit var seatNamesList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        seatBookReturnView = findViewById(R.id.layout_seat)

        val idDepart = intent.getStringExtra("idDepart")
        val flightId = intent.getIntExtra("idReturn", 0)
        val adultCount = intent.getIntExtra("adults", 0)
        val childCount = intent.getIntExtra("child", 0)
        val babyCount = intent.getIntExtra("baby", 0)
        val totalPassenger = intent.getIntExtra("totalPassenger", 0)
        val price = intent.getIntExtra("price", 0)
        Log.d("SeatBookActivity", "Seat: $flightId, Seat Return: $idDepart")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDestination")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        Log.d("SeatBookActivity", "Seat Class: $seatClassChoose")
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()

        passengerList.forEach { passenger ->
            Log.d("SeatBookReturnActivity", "Passenger: $passenger")
        }

        Log.d("SeatBookReturn", "Total Passenger: $totalPassenger")
        if (seatClassChoose != null) {
            getSeatData(flightId.toString(), seatClassChoose)
        }

        seatBookReturnView.setSelectSeatLimit(totalPassenger)

        binding.tvHeaderDestinationInfo.text =
            "($airportCityCodeDeparture > $airportCityCodeDestination - $seatClassChoose)"
        binding.layoutSeatbook.typeSeat.text = "$seatClassChoose"

        binding.btnSave.setOnClickListener {
            if (seatBookReturnView.getSelectedSeatCount() == totalPassenger) {
                val intent =
                    Intent(this, FlightDetailPriceActivity::class.java).apply {
                        putExtra("adults", adultCount)
                        putExtra("child", childCount)
                        putExtra("baby", babyCount)
                        putExtra("price", price)
                        putExtra("full_name", intent.getStringExtra("full_name"))
                        putExtra("number_phone", intent.getStringExtra("number_phone"))
                        putExtra("email", intent.getStringExtra("email"))
                        putExtra("family_name", intent.getStringExtra("family_name"))
                        putExtra("idReturn", flightId)
                        putExtra("idDepart", idDepart)
                        putExtra("tax", 300000.0)
                        putExtra("promo", 0.0)
                        putExtra("passenger_list", ArrayList(passengerList))
                    }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select seats for all passengers.", Toast.LENGTH_SHORT)
                    .show()
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
                    Log.d("formattedSeatStatus", formattedSeatStatus)
                    Log.d("formattedSeatNames", formattedSeatNames.toString())
                    Log.d("seatName", seatName.toString())
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
        seatBookReturnView.setSeatsLayoutString(formattedSeatStatus)
            .isCustomTitle(true)
            .setCustomTitle(formattedSeatNames)
            .setSeatLayoutPadding(2)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)

        seatBookReturnView.show()
    }

    private fun setClickListenerSeat(seatNamesList: List<String>) {
        seatBookReturnView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    if (selectedIdList.isNotEmpty()) {
                        val selectedSeatId = selectedIdList.first()
                        val seatName = seatNamesList[selectedSeatId - 1]
                        val row = seatName.last().toString()
                        val column = seatName.dropLast(1).toString()
                        Toast.makeText(
                            this@SeatBookReturnActivity,
                            "Seat Row : $row",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Toast.makeText(
                            this@SeatBookReturnActivity,
                            "Seat Column : $column",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val seatPassenger = SeatPassenger(seatRow = row, seatColumn = column)

                        val passengerIndex = selectedIdList.size - 1
                        if (passengerIndex < passengerList.size) {
                            passengerList[passengerIndex].seatReturn = seatPassenger
                            // Log the updated passenger info
                            Log.d(
                                "SeatBookActivity",
                                "Updated Passenger: ${passengerList[passengerIndex]}",
                            )
                        }

                        Toast.makeText(
                            this@SeatBookReturnActivity,
                            "Seat Row : $row, Seat Column : $column",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    /*if (selectedIdList.isNotEmpty()) {
                        val selectedSeatId = selectedIdList.last()
                        val seatName = seatNamesList[selectedSeatId - 1]
                        val row = seatName.last().toString()
                        val column = seatName.first().toString()
                        Toast.makeText(this@SeatBookActivity, "Seat Row : $row", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@SeatBookActivity, "Seat Column : $column", Toast.LENGTH_SHORT).show()
                        val seatPassenger = SeatPassenger(seatRow = row, seatColumn = column)

                        val nextPassengerIndex = passengerList.indexOfFirst { it.seatDeparture == null }
                        if (nextPassengerIndex != -1) {
                            passengerList[nextPassengerIndex].seatDeparture = seatPassenger
                            Log.d("SeatBookActivity", "Updated Passenger: ${passengerList[nextPassengerIndex]}")
                        }

                        Toast.makeText(this@SeatBookActivity, "Seat Row : $row, Seat Column : $column", Toast.LENGTH_SHORT).show()
                    }*/
                }

                override fun onBookedSeatClick(view: View) {
                }

                override fun onReservedSeatClick(view: View) {
                }
            },
        )
    }

    private fun getSeatNames(seats: List<Seat>): List<String> {
        return seats.sortedWith(compareBy({ it.seatColumn }, { it.seatRow }))
            .map { it.seatName }
    }
}
