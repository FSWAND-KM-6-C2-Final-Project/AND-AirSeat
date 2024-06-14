package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.BookingDetailHistory
import com.nafi.airseat.data.model.History
import com.nafi.airseat.data.model.HistoryArrivalAirport
import com.nafi.airseat.data.model.HistoryDepartureAirport
import com.nafi.airseat.data.model.HistoryFlight
import com.nafi.airseat.data.model.HistoryReturnArrivalAirport
import com.nafi.airseat.data.model.HistoryReturnDepartureAirport
import com.nafi.airseat.data.model.HistoryReturnFlight
import com.nafi.airseat.data.model.PassengerHistory
import com.nafi.airseat.data.model.SeatHistory
import com.nafi.airseat.data.source.network.model.history.ArrivalAirport
import com.nafi.airseat.data.source.network.model.history.Booking
import com.nafi.airseat.data.source.network.model.history.BookingDetail
import com.nafi.airseat.data.source.network.model.history.DepartureAirport
import com.nafi.airseat.data.source.network.model.history.Flight
import com.nafi.airseat.data.source.network.model.history.Passenger
import com.nafi.airseat.data.source.network.model.history.ReturnArrivalAirport
import com.nafi.airseat.data.source.network.model.history.ReturnDepartureAirport
import com.nafi.airseat.data.source.network.model.history.ReturnFlight
import com.nafi.airseat.data.source.network.model.history.Seat

fun Booking?.toHistoryModel() =
    History(
        bookingCode = this?.bookingCode.orEmpty(),
        bookingStatus = this?.bookingStatus.orEmpty(),
        classes = this?.classes.orEmpty(),
        createdAt = this?.createdAt.orEmpty(),
        duration = this?.duration.orEmpty(),
        flight = this?.flight.toHistoryFlightModel(),
        returnFlight = this?.returnFlight?.toHistoryReturnFlightModel(),
        totalAmount = this?.totalAmount ?: 0,
        bookingDetail = this?.bookingDetail.toBookingListDetailList()
    )

fun Flight?.toHistoryFlightModel() =
    HistoryFlight(
        arrivalAirport = this?.arrivalAirport.toHistoryArrivalAirportModel(),
        arrivalTime = this?.arrivalTime.orEmpty(),
        departureAirport = this?.departureAirport.toHistoryDepartureAirportModel(),
        departureTerminal = this?.departureTerminal.orEmpty(),
        departureTime = this?.departureTime.orEmpty(),
        flightNumber = this?.flightNumber.orEmpty(),
        information = this?.information.orEmpty(),
    )

fun ReturnFlight?.toHistoryReturnFlightModel() =
    HistoryReturnFlight(
        arrivalAirport = this?.arrivalAirport.toHistoryReturnArrivalAirportModel(),
        arrivalTime = this?.arrivalTime.orEmpty(),
        departureAirport = this?.departureAirport.toHistoryReturnDepartureAirportModel(),
        departureTerminal = this?.departureTerminal.orEmpty(),
        departureTime = this?.departureTime.orEmpty(),
        flightNumber = this?.flightNumber.orEmpty(),
        information = this?.information.orEmpty(),
    )

fun DepartureAirport?.toHistoryDepartureAirportModel() =
    HistoryDepartureAirport(
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
        airportName = this?.airportName.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
    )

fun ReturnDepartureAirport?.toHistoryReturnDepartureAirportModel() =
    HistoryReturnDepartureAirport(
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
        airportName = this?.airportName.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
    )

fun ArrivalAirport?.toHistoryArrivalAirportModel() =
    HistoryArrivalAirport(
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
        airportName = this?.airportName.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
    )

fun ReturnArrivalAirport?.toHistoryReturnArrivalAirportModel() =
    HistoryReturnArrivalAirport(
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
        airportName = this?.airportName.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
    )

fun  BookingDetail?.toBookingDetailHistory() =
    BookingDetailHistory(
        price = this?.price ?: 0,
        seat = this?.seat.toSeatHistory(),
        passenger = this?.passenger.toPassengerHistory()
    )

fun Seat?.toSeatHistory() =
    SeatHistory(
        classes = this?.classX.orEmpty(),
        seatName = this?.seatName.orEmpty()

    )

fun Passenger?.toPassengerHistory() =
    PassengerHistory(
        createdAt = this?.createdAt.orEmpty(),
        dob = this?.dob.orEmpty(),
        firstName = this?.firstName.orEmpty(),
        id = this?.id ?: 0,
        identificationCountry = this?.identificationCountry.orEmpty(),
        identificationExpired = this?.identificationExpired.orEmpty(),
        identificationNumber = this?.identificationNumber.orEmpty(),
        identificationType = this?.identificationType.orEmpty(),
        lastName = this?.lastName.orEmpty(),
        nationality = this?.nationality.orEmpty(),
        title = this?.title.orEmpty(),
        updatedAt = this?.createdAt.orEmpty()
    )

fun Collection<BookingDetail>?.toBookingListDetailList() =
    this?.map {
        it.toBookingDetailHistory()
    } ?: listOf()

fun Collection<Booking>?.toHistoryList() =
    this?.map {
        it.toHistoryModel()
    } ?: listOf()
