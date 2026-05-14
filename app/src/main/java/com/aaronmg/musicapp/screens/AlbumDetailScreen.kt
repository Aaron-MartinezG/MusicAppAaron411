package com.aaronmg.musicapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.aaronmg.musicapp.models.Album
import com.aaronmg.musicapp.services.AlbumsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun AlbumDetailScreen(
    id: String,
    navController: NavController = rememberNavController()
) {
    val BASE_URL = "https://musicapi.pjasoft.com/"
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val result = async(Dispatchers.IO) {
                retrofit.create(AlbumsService::class.java).getAlbumById(id)
            }
            album = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("AlbumDetailScreen", e.message.toString())
            isLoading = false
        }
    }

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = PurpleHeader)
        }
    } else if (album != null) {
        val currentAlbum = album!!
        // 10 canciones ficticias
        val tracks = List(10) { i -> "${currentAlbum.title} • Track ${i + 1}" }

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {

                // header con imagen full width y scrim morado
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .height(320.dp)
                    ) {
                        // Imagen del álbum
                        AsyncImage(
                            model = currentAlbum.image,
                            contentDescription = currentAlbum.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Scrim morado encima
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            PurpleHeader.copy(alpha = 0.4f),
                                            PurpleHeader.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )
                        // Flecha atrás y corazón arriba
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 48.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.clickable { navController.popBackStack() }
                            )
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = Color.White
                            )
                        }
                        // Título, artista y botones abajo del header
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = currentAlbum.title,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentAlbum.artist,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            // Botones play y shuffle
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Botón Play relleno
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(PurpleHeader, CircleShape)
                                        .clickable { isPlaying = !isPlaying },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                // Botón Shuffle contorno blanco
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shuffle,
                                        contentDescription = "Shuffle",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // ── CARD "About this album" ──
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "About this album",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = PurpleHeader
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentAlbum.description,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // chip artista
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = PurpleLight,
                            tonalElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Artist: ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PurpleHeader
                                )
                                Text(
                                    text = currentAlbum.artist,
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // canciones ficticias
                items(10) { index ->
                    TrackItem(
                        trackTitle = "${currentAlbum.title} • Track ${index + 1}",
                        artist = currentAlbum.artist,
                        imageUrl = currentAlbum.image
                    )
                }
            }

            // reproductor chiquito
            MiniPlayer(
                album = currentAlbum,
                isPlaying = isPlaying,
                onPlayPause = { isPlaying = !isPlaying },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("Error al cargar el álbum")
        }
    }
}

// cancion item
@Composable
fun TrackItem(trackTitle: String, artist: String, imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = trackTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .background(PurpleLight, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(trackTitle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(artist, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = Color.Gray
            )
        }
    }
}