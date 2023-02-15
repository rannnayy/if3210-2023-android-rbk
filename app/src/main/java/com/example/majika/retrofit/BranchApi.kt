package com.example.majika.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface BranchApi {
    @GET("branch")
    suspend fun getBranches() : Response<BranchResult>
}