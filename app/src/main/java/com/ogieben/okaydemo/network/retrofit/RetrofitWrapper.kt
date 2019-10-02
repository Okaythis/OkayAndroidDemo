package com.ogieben.okaydemo.network.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitWrapper {

    private val BASE_URL = ""

    fun createClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun handleTransactionEndpoints(): TransactionEndpoints {
        val retrofit: Retrofit = this.createClient()
        return retrofit.create(TransactionEndpoints::class.java)
    }
}