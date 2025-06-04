package com.example.composenetworkrequest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenetworkrequest.CherryBomb
import com.example.composenetworkrequest.api.req.getMusic
import com.example.composenetworkrequest.api.schema.Music

@Composable
fun GetScreen(padding: PaddingValues) {
    val sounds = remember { mutableStateListOf<Music>() }
    var success by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val (ok, data) = getMusic()
        success = ok
        sounds.clear()
        sounds.addAll(data)
    }

    LazyColumn(contentPadding = padding) {
        item {
            Spacer(Modifier.height(20.dp))
        }
        items(sounds) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        it.name,
                        fontSize = 25.sp,
                        fontFamily = CherryBomb
                    )
                    Text(it.metadata.description)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (sounds.isEmpty() && !success) {
            Card {
                Column(
                    Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No sounds found")
                }
            }
        } else if (sounds.isEmpty() && success) {
            CircularProgressIndicator()
        }
    }
}