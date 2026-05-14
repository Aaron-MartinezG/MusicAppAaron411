package com.aaronmg.musicapp.services

import com.aaronmg.musicapp.models.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumsService {
    @GET("api/albums")
    suspend fun getAllAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumById( @Path("id") id: String): Album
}