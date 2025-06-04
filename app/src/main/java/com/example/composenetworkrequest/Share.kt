package com.example.composenetworkrequest

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

enum class Screens {
    Get, Post
}

const val MUSIC_API_ENDPOINT = "https://skills-music-api-v3.eliaschen.dev"
const val KEY = "kitty-secret-key"

const val TODO_API_ENDPOINT = "https://skills-auth-todo-api.eliaschen.dev"

val CherryBomb = FontFamily(
    Font(R.font.cherry_bomb)
)

suspend fun GET(url: String, error: (String) -> Unit = {}, success: (String) -> Unit) {
    return withContext(Dispatchers.IO) {
        try {
            val req = URL(url).openConnection() as HttpURLConnection
            req.requestMethod = "GET"
            if (req.responseCode == 200) {
                val jsonText = req.inputStream.bufferedReader().use { it.readText() }
                success(jsonText)
            } else {
                error("Error: ${req.responseCode}")
            }
        } catch (e: Exception) {
            error("Error: $e")
        }
    }
}
