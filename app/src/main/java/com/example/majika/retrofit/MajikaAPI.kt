package com.example.majika.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface MajikaAPI {
    @GET("branch")
    suspend fun getBranches() : Response<BranchResponse>
    @POST("payment/{transactionId}")
    suspend fun pay(@Path("transactionId") transactionId: String) : Response<PaymentResponse>

    @GET("menu")
    suspend fun getMenu() : Response<MenuResponse>

    @GET("menu/food")
    suspend fun getFood() : Response<MenuResponse>

    @GET("menu/drink")
    suspend fun getDrink(): Response<MenuResponse>
}