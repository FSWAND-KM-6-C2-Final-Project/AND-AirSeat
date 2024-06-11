package com.nafi.airseat.data.source.network.model.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Booking(
    @SerializedName("booking_code")
    val bookingCode: String?,
    @SerializedName("bookingDetail")
    val bookingDetail: List<BookingDetail?>?,
    @SerializedName("booking_expired")
    val bookingExpired: String?,
    @SerializedName("booking_status")
    val bookingStatus: String?,
    @SerializedName("classes")
    val classes: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("flight")
    val flight: Flight?,
    @SerializedName("ordered_by_first_name")
    val orderedByFirstName: String?,
    @SerializedName("ordered_by_last_name")
    val orderedByLastName: String?,
    @SerializedName("ordered_by_phone_number")
    val orderedByPhoneNumber: String?,
    @SerializedName("payment_method")
    val paymentMethod: String?,
    @SerializedName("returnFlight")
    val returnFlight: Any?,
    @SerializedName("total_amount")
    val totalAmount: String?
)