package com.aaronmg.musicapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aaronmg.musicapp.models.Album
import com.aaronmg.musicapp.services.AlbumsService
import com.aaronmg.musicapp.ui.theme.MusicAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val PurpleHeader = Color(0xFF7B5EA7)
val PurpleLight = Color(0xFFEDE7F6)
val DarkPlayer = Color(0xFF1A0A2E)
@Composable
fun HomeScreen(
    navController: NavController = rememberNavController()
){
    val BASE_URL = "https://musicapi.pjasoft.com"
    var albums by remember {
        mutableStateOf(listOf(
            Album(id = "1", title = "The Dark Side of the Moon", artist = "Pink Floyd", description = "Album conceptual", image = ""),
            Album(id = "2", title = "Abbey Road", artist = "The Beatles", description = "Undécimo álbum", image = "")
        ))
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isPlaying by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        try {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val result = async(Dispatchers.IO) {
                val albumService = retrofitBuilder.create(AlbumsService::class.java)
                albumService.getAllAlbums()
            }
            Log.i("AlbumsScreen", result.await().toString())
            albums = result.await()
            isLoading = false
        }
        catch (e: Exception){
            Log.e("AlbumsScreen", e.message.toString())
            isLoading = false
        }
    }
    if (isLoading){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PurpleHeader, PurpleLight, PurpleLight.copy(alpha = 0.1f))
                        )
                    )
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 48.dp, bottom = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PurpleHeader
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Good Morning!",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp
                            )
                            Text(
                                modifier = Modifier.padding(bottom = 15.dp),
                                text = "Aarón Martínez",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ProductsScreenPreview(){
    MusicAppTheme() {
        HomeScreen()
    }
}