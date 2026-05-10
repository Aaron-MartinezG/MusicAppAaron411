package com.aaronmg.musicapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aaronmg.musicapp.models.Album
import com.aaronmg.musicapp.services.AlbumsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(10.dp),
    navController: NavController = rememberNavController()
){
    val BASE_URL = "https://musicapi.pjasoft.com"
    var albums by remember {
        mutableStateOf(listOf<Album>())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }
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
        Column(

        ) { }
    }
}