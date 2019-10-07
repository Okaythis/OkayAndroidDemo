package com.ogieben.okaydemo.network.retrofit

import com.ogieben.okaydemo.data.model.OkayLinking
import io.reactivex.internal.operators.flowable.FlowableAny
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionEndpoints {

    @POST("/link")
    fun linkUser(@Query("userExternalId") userExternalId: String): FlowableAny<OkayLinking>

    @POST("/auth")
    fun authorizeTransaction()
}