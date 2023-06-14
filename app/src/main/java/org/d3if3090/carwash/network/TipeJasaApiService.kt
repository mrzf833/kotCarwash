package org.d3if3090.carwash.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3090.carwash.model.TipeJasa
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "mrzf833/static-api/main/kot-carwash/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TipeJasaApiService {
    @GET("tipe-jasas.json")
    suspend fun getTipeJasa(): List<TipeJasa>
}

object TipeJasaApi {
    val service: TipeJasaApiService by lazy {
        retrofit.create(TipeJasaApiService::class.java)
    }
}