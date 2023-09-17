package com.example.apigpt.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apigpt.api.GptApi
import com.example.apigpt.model.GptRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class ContentViewModel : ViewModel() {

    var answer by mutableStateOf ("")
        private set

    var isLoading by mutableStateOf (false)
        private set

    val retrofit = Retrofit.Builder ()
        .baseUrl ("https://api.openai.com")
        .addConverterFactory (GsonConverterFactory.create ())
        .build ()

    val api = retrofit.create (GptApi::class.java)

    fun sendRequest(question: String, role: String, conteudoLido: String? = null) {
        val systemPrompt = "You must generate the probabilities of whether the following sentence refers one of the commands \"action\", \"question\" or \"neither\" on the management of any system file, and answer back in one word the command with the highest probability:"
        val userPrompt = "Role User: \"$question\""

        val request = GptRequest (
            prompt = "$systemPrompt\n\n$userPrompt",
            max_tokens = 1000,
            model = "text-davinci-003"
        )

        viewModelScope.launch (Dispatchers.IO) {
            isLoading = true
            val call = api.getCompletion (request)
            val response = call.execute ()
            isLoading = false

            if (response.isSuccessful) {
                val systemResponse = response.body()?.choices?.first()?.text ?: ""
                handleSystemResponse(systemResponse)
            } else {
                answer = "An error occurred: ${response.errorBody()?.string()}"
            }
        }
    }

    private fun handleSystemResponse(systemResponse: String) {
        val lowerCaseResponse = systemResponse.trim().toLowerCase()

        when {
            lowerCaseResponse.contains("action") -> {
                // A resposta menciona "action", você pode perguntar mais detalhes ao usuário
                answer = "functionality action coming soon"
            }
            lowerCaseResponse.contains("question") -> {
                // A resposta menciona "question", você pode pedir mais detalhes
                answer = "what is the file and what is the question?"
            }
            lowerCaseResponse.contains("neither") -> {
                // A resposta menciona "neither", você pode responder de acordo
                answer = "functionality neither coming soon"
            }
            else -> {
                // Nenhuma das palavras-chave encontradas, você pode lidar com outros casos ou erros
                answer = "Desculpe, ocorreu um erro."
            }
        }
    }

}


















/*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apigpt.api.GptApi
import com.example.apigpt.model.GptRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue





class ContentViewModel : ViewModel() {

        var answer by mutableStateOf ("")
            private set

        var isLoading by mutableStateOf (false)
            private set

        val retrofit = Retrofit.Builder ()
            .baseUrl ("https://api.openai.com")
            .addConverterFactory (GsonConverterFactory.create ())
            .build ()

        val api = retrofit.create (GptApi::class.java)

        fun sendRequest(question: String, role: String, conteudoLido: String? = null) {
            val prompt = buildPrompt(question, role, conteudoLido)

            val request = GptRequest (
                prompt = prompt,
                max_tokens = 1000,
                model = "text-davinci-003"
            )

            viewModelScope.launch (Dispatchers.IO) {
                isLoading = true
                val call = api.getCompletion (request)
                val response = call.execute ()
                isLoading = false

                if (response.isSuccessful) {
                    answer = response.body()?.choices?.first()?.text ?: ""
                } else {
                    answer = "An error occurred: ${response.errorBody()?.string()}"
                }
            }
        }

        private fun buildPrompt(question: String, role: String, conteudoLido: String?): String {
            return when (role) {
                "system" -> "Role System: You must generate the probabilities of whether the following sentence refers one of the commands \"action\", \"question\" or \"neither\" on the management of any system file, and answer back in one word the command with the highest probability:\n\nRole User: \"$question\""
                "user" -> {
                    if (conteudoLido != null) {
                        "Role User: \"$question\" \"$conteudoLido\""
                    } else {
                        "Role User: \"$question\""
                    }
                }
                else -> question
            }
        }
}

 */

