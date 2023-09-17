package com.example.apigpt.model

data class GptRequest(
    val prompt: String,
    val max_tokens: Int,
    val model: String
)

data class GptResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)
