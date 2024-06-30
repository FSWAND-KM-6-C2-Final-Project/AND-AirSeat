package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.AirlineHistory
import com.c2.airseat.data.model.BookingDetailHistory
import com.c2.airseat.data.model.History
import com.c2.airseat.data.model.HistoryArrivalAirport
import com.c2.airseat.data.model.HistoryDepartureAirport
import com.c2.airseat.data.model.HistoryFlight
import com.c2.airseat.data.model.HistoryReturnArrivalAirport
import com.c2.airseat.data.model.HistoryReturnDepartureAirport
import com.c2.airseat.data.model.HistoryReturnFlight
import com.c2.airseat.data.model.PassengerHistory
import com.c2.airseat.data.model.SeatHistory
import com.c2.airseat.data.source.network.model.history.Airline
import com.c2.airseat.data.source.network.model.history.ArrivalAirport
import com.c2.airseat.data.source.network.model.history.Booking
import com.c2.airseat.data.source.network.model.history.BookingDetail
import com.c2.airseat.data.source.network.model.history.DepartureAirport
import com.c2.airseat.data.source.network.model.history.Flight
import com.c2.airseat.data.source.network.model.history.Passenger
import com.c2.airseat.data.source.network.model.history.ReturnArrivalAirport
import com.c2.airseat.data.source.network.model.history.ReturnDepartureAirport
import com.c2.airseat.data.source.network.model.history.ReturnFlight
import com.c2.airseat.data.source.network.model.history.Seat

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
        bookingDetail = this?.bookingDetail.toBookingListDetailList(),
        paymentUrl = this?.paymentUrl.orEmpty(),
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
        airline = this?.airline.toAirlineHistory(),
    )

fun Airline?.toAirlineHistory() =
    AirlineHistory(
        airlineName = this?.airlineName.orEmpty(),
        airlinePicture = this?.airlinePicture.orEmpty(),
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
        airline = this?.airline.toAirlineHistory(),
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

fun BookingDetail?.toBookingDetailHistory() =
    BookingDetailHistory(
        price = this?.price ?: 0,
        seat = this?.seat.toSeatHistory(),
        passenger = this?.passenger.toPassengerHistory(),
    )

fun Seat?.toSeatHistory() =
    SeatHistory(
        classes = this?.classX.orEmpty(),
        seatName = this?.seatName.orEmpty(),
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
        updatedAt = this?.createdAt.orEmpty(),
        passengerType = this?.passengerType.orEmpty(),
    )

fun Collection<BookingDetail>?.toBookingListDetailList() =
    this?.map {
        it.toBookingDetailHistory()
    } ?: listOf()

fun Collection<Booking>?.toHistoryList() =
    this?.map {
        it.toHistoryModel()
    } ?: listOf()
