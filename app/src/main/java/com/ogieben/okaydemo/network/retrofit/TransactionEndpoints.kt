package com.ogieben.okaydemo.network.retrofit

import com.ogieben.okaydemo.data.model.AuthorizationResponse
import com.ogieben.okaydemo.data.model.OkayLinking
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query


interface TransactionEndpoints {

    @POST("/link")
    fun linkUser(@Query("userExternalId") userExternalId: String?): Call<OkayLinking>

    @POST("/auth")
    fun authorizeTransaction(@Query("userExternalId") userExternalId: String?): Call<AuthorizationResponse>

    @POST("/auth/pin")
    fun authorizeOTPTransaction(@Query("userExternalId") userExternalId: String?): Call<AuthorizationResponse>
}