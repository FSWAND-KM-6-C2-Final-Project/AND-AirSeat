package com.c2.airseat.data.source.network.model.booking

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Booking(
    @SerializedName("booking_code")
    val bookingCode: String,
    @SerializedName("booking_expired")
    val bookingExpired: String?,
    @SerializedName("booking_status")
    val bookingStatus: String?,
    @SerializedName("discount_id")
    val discountId: Int?,
    @SerializedName("flight_id")
    val flightId: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("ordered_by_email")
    val orderedByEmail: String?,
    @SerializedName("ordered_by_first_name")
    val orderedByFirstName: String?,
    @SerializedName("ordered_by_last_name")
    val orderedByLastName: String?,
    @SerializedName("ordered_by_phone_number")
    val orderedByPhoneNumber: String?,
    @SerializedName("payment_id")
    val paymentId: String?,
    @SerializedName("payment_method")
    val paymentMethod: String?,
    @SerializedName("return_flight_id")
    val returnFlightId: Any?,
    @SerializedName("total_amount")
    val totalAmount: Int?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int?,
)
