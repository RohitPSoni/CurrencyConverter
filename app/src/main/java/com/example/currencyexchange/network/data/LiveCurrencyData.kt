package com.example.currencyexchange.network.data

import com.google.gson.annotations.SerializedName

data class LiveCurrencyData(
    @SerializedName("success") val success: Boolean,
    @SerializedName("terms") val terms: String,
    @SerializedName("privacy") val privacy: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("source") val source: String,
    @SerializedName("quotes") val currencies: Map<String, Double>,
    @SerializedName("error") val error: Error?
)

data class Error(
    @SerializedName("code") val code: Int,
    @SerializedName("info") val info: String
)