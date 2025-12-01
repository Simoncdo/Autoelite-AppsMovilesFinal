package com.example.autoelite.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
data class MonedaResponse(
    @SerializedName("rates")
    val tasas: Map<String, Double>
)
interface CurrencyService {
    @GET("latest/CLP")
    suspend fun obtenerTasas(): MonedaResponse
}
object CurrencyClient {
    private const val BASE_URL = "https://open.er-api.com/v6/"

    val service: CurrencyService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService::class.java)
    }
}