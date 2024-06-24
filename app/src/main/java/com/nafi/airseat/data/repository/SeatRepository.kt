package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.seat.SeatDataSource
import com.nafi.airseat.data.mapper.toSeats
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceed
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SeatRepository {
    fun getSeats(
        flightId: String,
        seatClass: String,
    ): Flow<ResultWrapper<List<Seat>>>

    fun getFormattedSeatData(
        flightId: String,
        seatClassChoose: String,
    ): Flow<ResultWrapper<Triple<String, List<String>, List<String>>>>
}

class SeatRepositoryImpl(private val dataSource: SeatDataSource) : SeatRepository {
    override fun getSeats(
        flightId: String,
        seatClass: String,
    ): Flow<ResultWrapper<List<Seat>>> {
        return proceedFlow { dataSource.getSeats(flightId, seatClass).data.seats.toSeats() }
    }

    override fun getFormattedSeatData(
        flightId: String,
        seatClassChoose: String,
    ): Flow<ResultWrapper<Triple<String, List<String>, List<String>>>> {
        val seatClassFormat =
            when (seatClassChoose) {
                "Economy" -> "economy"
                "Premium Economy" -> "premium_economy"
                "Business" -> "business"
                "First Class" -> "first_class"
                else -> seatClassChoose.toLowerCase().replace(" ", "_")
            }

        return getSeats(flightId, seatClassFormat).map {
            proceed {
                val formattedSeatStatus: String
                val formattedSeatNames: List<String>
                val sortedSeats = it.payload?.sortedWith(compareBy({ it.seatColumn }, { it.seatRow })).orEmpty()
                val seatNames = sortedSeats.map { it.seatName }
                val seatStatus = sortedSeats.joinToString("") { it.seatStatusAndroid }

                when (seatClassChoose) {
                    "Economy" -> {
                        formattedSeatNames = formatSeatNamesEconomy(it.payload.orEmpty())
                        formattedSeatStatus = formatSeatStatusEconomy(seatStatus)
                    }

                    "First Class" -> {
                        formattedSeatNames = formatSeatNamesFirstClass(it.payload.orEmpty())
                        formattedSeatStatus = formatSeatStatusFirstClass(seatStatus)
                    }

                    else -> {
                        formattedSeatNames = formatSeatNamesPremiumAndBusiness(it.payload.orEmpty())
                        formattedSeatStatus = formatSeatStatusPremiumAndBusiness(seatStatus)
                    }
                }
                Triple(formattedSeatStatus, formattedSeatNames, seatNames)
            }
        }
    }

    private fun formatSeatStatusEconomy(seatStatus: String): String {
        val formattedBuilder = StringBuilder()
        formattedBuilder.append("/")
        var isSlash = true
        for (i in seatStatus.indices) {
            if (i % 3 == 0 && i != 0) {
                if (isSlash) {
                    formattedBuilder.append("_")
                } else {
                    formattedBuilder.append("/")
                }
                isSlash = !isSlash
            }
            formattedBuilder.append(seatStatus[i])
        }
        return formattedBuilder.toString()
    }

    private fun formatSeatNamesEconomy(seats: List<Seat>): List<String> {
        val formattedList = mutableListOf<String>()
        formattedList.add("\"/\"")

        seats.forEachIndexed { index, seat ->
            formattedList.add(seat.seatName)
            if ((index + 1) % 3 == 0) {
                formattedList.add("\"\"")
            }
        }
        formattedList.add("\"/\"")
        return formattedList
    }

    private fun formatSeatStatusPremiumAndBusiness(seatStatus: String): String {
        val formattedBuilder = StringBuilder()
        formattedBuilder.append("/")
        var isSlash = true
        for (i in seatStatus.indices) {
            if (i % 2 == 0 && i != 0) {
                if (isSlash) {
                    formattedBuilder.append("_")
                } else {
                    formattedBuilder.append("/")
                }
                isSlash = !isSlash
            }
            formattedBuilder.append(seatStatus[i])
        }
        return formattedBuilder.toString()
    }

    private fun formatSeatNamesPremiumAndBusiness(seats: List<Seat>): List<String> {
        val formattedList = mutableListOf<String>()
        formattedList.add("\"/\"")

        seats.forEachIndexed { index, seat ->
            formattedList.add(seat.seatName)
            if ((index + 1) % 2 == 0) {
                formattedList.add("\"\"")
            }
        }

        formattedList.add("\"/\"")
        return formattedList
    }

    private fun formatSeatStatusFirstClass(seatStatus: String): String {
        val formattedBuilder = StringBuilder()
        formattedBuilder.append("/")

        seatStatus.forEachIndexed { index, char ->
            if (index != 0) {
                if (index % 2 == 0) {
                    formattedBuilder.append("/")
                } else {
                    formattedBuilder.append("_")
                }
            }
            formattedBuilder.append(char)
        }
        return formattedBuilder.toString()
    }

    private fun formatSeatNamesFirstClass(seats: List<Seat>): List<String> {
        val formattedList = mutableListOf<String>()
        formattedList.add("/")

        seats.chunked(2).forEachIndexed { index, chunk ->
            if (index > 0) {
                formattedList.add("/")
            }
            chunk.forEach { seat ->
                formattedList.add(seat.seatName)
                formattedList.add("")
            }
            formattedList.removeLast()
        }
        return formattedList
    }
}
