package com.example.composenetworkrequest.api.req

import android.util.Log
import com.example.composenetworkrequest.MUSIC_API_ENDPOINT
import com.example.composenetworkrequest.KEY
import com.example.composenetworkrequest.api.schema.MetaData
import com.example.composenetworkrequest.api.schema.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

suspend fun getMusic(): Pair<Boolean, List<Music>> {
    return withContext(Dispatchers.IO) {
        val req = URL("$MUSIC_API_ENDPOINT/sounds").openConnection() as HttpURLConnection
        req.addRequestProperty("X-API-Key", KEY)
        req.requestMethod = "GET"

        return@withContext try {
            val code = req.responseCode
            if (code == 200) {
                val jsonText = req.inputStream.bufferedReader().use { it.readText() }
                Pair(true, parse(jsonText))
            } else {
                Log.i("JSON", "error")
                Pair(false, emptyList())
            }
        } catch (e: Exception) {
            Log.i("JSON", "error: $e")
            Pair(false, emptyList())
        }
    }
}

private fun parse(text: String): List<Music> {
    val jsonObject = JSONArray(text)
    return List(jsonObject.length()) {
        val item = jsonObject.getJSONObject(it)
        return@List Music(
            id = item.getInt("id"),
            name = item.getString("name"),
            metadata = MetaData(
                item.getJSONObject("metadata").getString("description")
            )
        )
    }
}