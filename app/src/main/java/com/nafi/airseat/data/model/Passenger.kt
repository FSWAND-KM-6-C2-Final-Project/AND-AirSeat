package com.nafi.airseat.data.model

data class Passenger(
    val id: Int,
    val firstName: String,
    val familyName: String,
    val title: String,
    val dob: String,
    val nationality: String,
    val identificationType: String,
    val identificationNumber: String,
    val identificationCountry: String,
    val identificationExpired: String,
)
