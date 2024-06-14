package com.nafi.airseat.presentation.resultsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nafi.airseat.data.model.Flight
import com.nafi.airseat.databinding.ItemResultSearchTicketBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ResultSearchAdapter(private val listener: (Flight) -> Unit) :
    RecyclerView.Adapter<ResultSearchAdapter.ItemFlightViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Flight>() {
                override fun areItemsTheSame(
                    oldItem: Flight,
                    newItem: Flight,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Flight,
                    newItem: Flight,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Flight>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemFlightViewHolder {
        val binding = ItemResultSearchTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFlightViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ItemFlightViewHolder,
        position: Int,
    ) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemFlightViewHolder(
        private val binding: ItemResultSearchTicketBinding,
        val itemClick: (Flight) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Flight) {
            with(item) {
                val (departureDate, departureTimes) = processDateTime(item.departureTime)
                val (arrivalDate, arrivalTimes) = processDateTime(item.arrivalTime)
                binding.tvDeparturePlace.text = item.departureAirport.airportCityCode
                binding.tvArrivalPlace.text = item.arrivalAirport.airportCityCode
                binding.tvDepartureTime.text = departureTimes
                binding.tvDuration.text = calculateDuration(item.departureTime, item.arrivalTime)
                binding.tvArrivalTime.text = arrivalTimes
                binding.imgAirlineLogo.load(item.airline.airlinePicture) {
                    crossfade(true)
                }
                binding.tvAirlineInfo.text = item.airline.airlineName
                binding.tvTicketPrice.text = item.pricePremiumEconomy.toString()
                itemView.setOnClickListener { itemClick(this) }
            }
        }

        fun processDateTime(departureTime: String): Pair<String, String> {
            val parts = departureTime.split("T")
            val date = parts[0]
            var times = parts[1].removeSuffix(".000Z")

            if (times.endsWith(":00")) {
                times = times.replace(Regex(":00$"), "")
            }

            return Pair(date, times)
        }

        fun formatTime(timeString: String): String {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = inputFormat.parse(timeString)
            return outputFormat.format(time!!)
        }

        fun calculateDuration(
            departureTime: String,
            arrivalTime: String,
        ): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            try {
                val departureDateTime = sdf.parse(departureTime)
                val arrivalDateTime = sdf.parse(arrivalTime)

                // Menghitung selisih waktu dalam milidetik
                val diffInMillis = (arrivalDateTime?.time ?: 0) - (departureDateTime?.time ?: 0)

                // Konversi milidetik ke jam dan menit
                val hours = diffInMillis / (1000 * 60 * 60)
                val minutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60)

                return "${hours}h ${minutes}m"
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }
    }
}
