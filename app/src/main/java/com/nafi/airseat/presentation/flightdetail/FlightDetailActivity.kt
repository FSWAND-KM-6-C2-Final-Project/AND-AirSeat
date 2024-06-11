package com.nafi.airseat.presentation.flightdetail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.R
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.data.model.FlightDetailPrice
import com.nafi.airseat.databinding.ActivityFlightDetailBinding
import com.nafi.airseat.presentation.payment.WebViewMidtransActivity
import com.nafi.airseat.utils.toIndonesianFormat

class FlightDetailActivity : AppCompatActivity() {
    private val binding: ActivityFlightDetailBinding by lazy {
        ActivityFlightDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adults = intent.getIntExtra("adults", 0)
        val baby = intent.getIntExtra("baby", 0)
        val child = intent.getIntExtra("child", 0)
        val adultsPrice = intent.getDoubleExtra("adultsPrice", 0.0)
        val childPrice = intent.getDoubleExtra("childPrice", 0.0)
        val babyPrice = intent.getDoubleExtra("babyPrice", 0.0)
        val tax = intent.getDoubleExtra("tax", 0.0)
        val promo = intent.getDoubleExtra("promo", 0.0)

        val flightDetailPrice =
            getDummyFlightDetail(
                adults,
                child,
                baby,
                adultsPrice,
                childPrice,
                babyPrice,
                tax,
                promo,
            )
        populateFlightDetails(flightDetailPrice)

        binding.cvSectionCheckout.btnPayment.setOnClickListener {
            openUrlFromWebView()
        }
        binding.ibBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDummyFlightDetail(
        adults: Int,
        child: Int,
        baby: Int,
        adultsPrice: Double,
        childPrice: Double,
        babyPrice: Double,
        tax: Double,
        promo: Double,
    ): FlightDetail {
        val totalAdultPrice = if (adults == 0) 0.0 else adults * adultsPrice
        val totalChildPrice = if (child == 0) 0.0 else child * childPrice
        val totalBabyPrice = if (baby == 0) 0.0 else baby * babyPrice

        val totalPrice = totalChildPrice + totalChildPrice + totalBabyPrice + tax - promo

        val flightDetailPrice =
            FlightDetailPrice(
                destination = "Jakarta -> Melbourne",
                flightTime = "(4h 0m)",
                adults = adults,
                adultsPrice = totalAdultPrice,
                baby = baby,
                babyPrice = totalBabyPrice,
                child = child,
                childPrice = totalChildPrice,
                tax = tax,
                promo = promo,
                totalPrice = totalPrice,
            )

        return FlightDetail(
            departureTime = "07:00",
            departureDate = "3 March 2023",
            departureAirport = "Soekarno Hatta - Domestic Terminal 1A",
            airplane = "Jet Air - Economy",
            codeAirplane = "JT - 203",
            baggage = "20 kg",
            cabinBaggage = "7 kg",
            flightEntertainment = "In Flight Entertainment",
            arrivalTime = "11:00",
            arrivalDate = "3 March 2023",
            arrivalAirport = "Melbourne International Airport",
            flightDetailPrice = flightDetailPrice,
        )
    }

    private fun populateFlightDetails(flightDetail: FlightDetail) {
        binding.layoutFlightInfo.tvDepartureTime.text = flightDetail.departureTime
        binding.layoutFlightInfo.tvDepartureDate.text = flightDetail.departureDate
        binding.layoutFlightInfo.tvDepartureAirport.text = flightDetail.departureAirport
        binding.layoutFlightInfo.tvAirplane.text = flightDetail.airplane
        binding.layoutFlightInfo.tvCodeAirplane.text = flightDetail.codeAirplane
        binding.layoutFlightInfo.tvBaggage.text = "Baggage: ${flightDetail.baggage}"
        binding.layoutFlightInfo.tvCabinBaggage.text = "Cabin Baggage: ${flightDetail.cabinBaggage}"
        binding.layoutFlightInfo.tvFlightEntertain.text = flightDetail.flightEntertainment
        binding.layoutFlightInfo.tvArrivalTime.text = flightDetail.arrivalTime
        binding.layoutFlightInfo.tvArrivalDate.text = flightDetail.arrivalDate
        binding.layoutFlightInfo.tvArrivalAirport.text = flightDetail.arrivalAirport

        val flightDetailPrice = flightDetail.flightDetailPrice
        binding.destination.text = flightDetailPrice.destination
        binding.flightTime.text = flightDetailPrice.flightTime
        binding.tvAdults.text = "${flightDetailPrice.adults} Adults"
        binding.tvChild.text = "${flightDetailPrice.child} Child"
        binding.tvBaby.text = "${flightDetailPrice.baby} Baby"
        binding.tvDetailPrice.text = getString(R.string.detail_price)
        binding.tvAdultPrice.text = flightDetailPrice.adultsPrice.toIndonesianFormat()
        binding.tvChildPrice.text = flightDetailPrice.childPrice.toIndonesianFormat()
        binding.tvBabyPrice.text = flightDetailPrice.babyPrice.toIndonesianFormat()
        binding.tvTaxAmount.text = flightDetailPrice.tax.toIndonesianFormat()
        binding.tvPromoAmount.text = flightDetailPrice.promo.toIndonesianFormat()
        binding.cvSectionCheckout.totalPrice.text = flightDetailPrice.totalPrice.toIndonesianFormat()
    }

    private fun openUrlFromWebView() {
        val intent = Intent(this, WebViewMidtransActivity::class.java)
        intent.putExtra(
            "URL",
            "https://sample-demo-dot-midtrans-support-tools.et.r.appspot.com/snap-redirect/",
        )
        startActivity(intent)
    }
}
