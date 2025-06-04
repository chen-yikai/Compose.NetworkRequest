package com.example.composenetworkrequest.api.req

import android.util.Log
import com.example.composenetworkrequest.GET
import com.example.composenetworkrequest.TODO_API_ENDPOINT
import com.example.composenetworkrequest.api.schema.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

suspend fun getTodo(): Pair<Boolean, List<Todo>> {
    var todos: List<Todo> = emptyList()
    var success = true
    GET("$TODO_API_ENDPOINT/todos", error = { success = false }) { text ->
        todos = parse(text)
    }
    return Pair(success, todos)
}

suspend fun createTodo(todo: Todo) {
    withContext(Dispatchers.IO) {
        try {
            val req = URL("$TODO_API_ENDPOINT/todos").openConnection() as HttpURLConnection
            req.requestMethod = "POST"
            val content = JSONObject()
            content.put("name", todo.name)
            content.put("type", todo.type)
            content.put("time", "00:00")
            content.put("day", "1")
            Log.i("JSON", content.toString())
            req.doOutput = true
            req.outputStream.apply {
                write(content.toString().toByteArray())
                flush()
                close()
            }
            if (req.responseCode == 200) {
                val jsonText = req.inputStream.bufferedReader().use { it.readText() }
                Log.i("CreateTodo", jsonText)
            } else {
                Log.i("CreateTodoError", "CreateTodo error: ${req.responseCode}")
            }

        } catch (e: Exception) {
            Log.i("CreateTodoError", "CreateTodo error:$e")
        }
    }
}

suspend fun deleteTodo(id: Int) {
    withContext(Dispatchers.IO) {
        try {
            val req = URL("$TODO_API_ENDPOINT/todos").openConnection() as HttpURLConnection
            req.requestMethod = "DELETE"
            val content = JSONObject()
            content.put("id", id)
            req.doOutput = true
            req.outputStream.apply {
                write(content.toString().toByteArray())
                flush()
                close()
            }

            if (req.responseCode == 200) {
                val jsonText = req.inputStream.bufferedReader().use { it.readText() }
                Log.i("DeleteTodo", jsonText)
            } else {
                Log.i("DeleteTodoError", "DeleteTodo error: ${req.responseCode}")
            }
        } catch (e: Exception) {
            Log.i("DeleteTodoError", "DeleteTodo error: $e")
        }
    }
}

private fun parse(text: String): List<Todo> {
    val jsonObject = JSONArray(text)

    return List(jsonObject.length()) {
        val item = jsonObject.getJSONObject(it)
        Todo(
            id = item.getInt("id"),
            name = item.getString("name"),
            type = item.getString("type"),
            time = item.getString("time"),
            day = item.getString("day")
        )
    }
}