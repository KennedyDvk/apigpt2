package com.example.apigpt.api

import com.example.apigpt.model.GptRequest
import com.example.apigpt.model.GptResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GptApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("/v1/completions")
    fun getCompletion(
        @Body requestBody: GptRequest
    ): Call<GptResponse>
}
