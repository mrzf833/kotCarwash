package org.d3if3090.carwash.network

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "mrzf833/static-api/main/kot-carwash/"

interface MainApiService {

}

object MainApi {
    fun getCarLogoUrl(): String {
        return "${BASE_URL}car_wash.png"
    }
}