package com.nafi.airseat.presentation.detailflight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.databinding.ActivityDetailFlightBinding
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFlightActivity : AppCompatActivity() {
    private val binding: ActivityDetailFlightBinding by lazy {
        ActivityDetailFlightBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailFlightViewModel by viewModel {
        parametersOf(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val id = intent.getStringExtra("id")
        preceedDetailTicket(id.toString())
        // viewModel.idExtras?.let { preceedDetailTicket(it.toString()) }
    }

    companion object {
        const val EXTRAS_ITEM = "EXTRAS_ITEM"

        fun startActivity(
            context: Context,
            id: String,
        ) {
            val intent = Intent(context, DetailFlightActivity::class.java)
            intent.putExtra(EXTRAS_ITEM, id)
            context.startActivity(intent)
        }
    }

    private fun preceedDetailTicket(id: String) {
        viewModel.getDetailFlight(id).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        bindView(it)
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    }
                },
                doOnLoading = {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                },
                doOnEmpty = {
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun bindView(detail: FlightDetail) {
        detail.let {
            binding.layoutDetail.tvDepartureTime.text = it.departureTime
            binding.layoutDetail.tvDepartureDate.text = it.departureTime
            binding.layoutDetail.tvDeparturePlace.text = it.departureTerminal
            /*binding.layoutDetail.imgAirlineLogo.load(it.a) {
                crossfade(true)
            }*/
            // binding.layoutDetail.tvAirlineName.text = it.airlineName
            // binding.layoutDetail.tvAirlineCode.text = it.airlineCode
            binding.layoutDetail.tvAirlineCode.text = it.flightNumber
            binding.layoutDetail.tvFlightType.text = it.information
            binding.layoutDetail.tvArrivalTime.text = it.arrivalTime
            binding.layoutDetail.tvArrivalDate.text = it.arrivalTime
            // binding.layoutDetail.tvArrivalPlace.text = it.arrivalTerminal
            binding.tvTotalPrice.text = it.pricePremiumEconomy
        }
    }
}
