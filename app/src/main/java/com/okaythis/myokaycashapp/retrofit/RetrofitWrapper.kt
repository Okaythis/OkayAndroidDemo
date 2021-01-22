package com.okaythis.myokaycashapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitWrapper {

    private val BASE_URL = "http://okayserversample.herokuapp.com/"

    fun createClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun handleTransactionEndpoints(): TransactionEndpoints {
        val retrofit: Retrofit = this.createClient()
        return retrofit.create(TransactionEndpoints::class.java)
    }
}