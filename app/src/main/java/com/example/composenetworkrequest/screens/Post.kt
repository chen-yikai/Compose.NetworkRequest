package com.example.composenetworkrequest.screens

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenetworkrequest.CherryBomb
import com.example.composenetworkrequest.api.req.createTodo
import com.example.composenetworkrequest.api.req.deleteTodo
import com.example.composenetworkrequest.api.req.getTodo
import com.example.composenetworkrequest.api.schema.Todo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostScreen(padding: PaddingValues) {
    val todos = remember { mutableStateListOf<Todo>() }
    val scope = rememberCoroutineScope()
    var success by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }

    suspend fun updateTodo() {
        loading = true
        val (ok, data) = getTodo()
        success = ok
        todos.clear()
        todos.addAll(data)
        loading = false
    }

    LaunchedEffect(Unit) {
        updateTodo()
    }


    LazyColumn(contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())) {
        stickyHeader {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                Column(Modifier.padding(10.dp).padding(top = padding.calculateTopPadding())) {
                    var name by remember { mutableStateOf("") }
                    var type by remember { mutableStateOf("") }

                    Text("Todo List", fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = CherryBomb, color = MaterialTheme.colorScheme.primary)
                    TextField(
                        name,
                        onValueChange = { name = it },
                        label = { Text("Name", fontFamily = CherryBomb) },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = CherryBomb, fontSize = 25.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                    )
                    TextField(
                        type,
                        onValueChange = { type = it },
                        label = { Text("Type", fontFamily = CherryBomb) },
                        textStyle = TextStyle(fontFamily = CherryBomb, fontSize = 20.sp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                    )
                    Spacer(Modifier.height(15.dp))
                    Button(onClick = {
                        scope.launch {
                            createTodo(Todo(0, name, type, "", ""))
                            updateTodo()
                            name = ""
                            type = ""
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Create Todo", fontFamily = CherryBomb)
                    }
                }
            }
        }
        item{
            Spacer(Modifier.height(15.dp))
        }
        items(todos) {
            Card(modifier = Modifier.padding(10.dp).padding(horizontal = 2.dp)) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                ) {
                    Text(it.name, fontSize = 25.sp, fontFamily = CherryBomb)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            it.type,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                        FilledTonalButton(onClick = {
                            scope.launch {
                                deleteTodo(it.id)
                                updateTodo()
                            }
                        }) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (todos.isEmpty() && !loading) {
            Card (modifier = Modifier.padding(horizontal = 20.dp)){
                Column(
                    Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No todos found", fontFamily = CherryBomb)
                }
            }
        } else if (todos.isEmpty() && loading) {
            CircularProgressIndicator()
        }
    }
}