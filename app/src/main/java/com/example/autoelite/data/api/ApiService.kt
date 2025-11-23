package com.example.autoelite.data.api

import com.example.autoelite.data.model.Auto
import com.example.autoelite.data.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/autos")
    suspend fun getAutos(): List<Auto>

    @GET("api/autos/{id}")
    suspend fun getAutoById(@Path("id") id: Long): Response<Auto>

    @POST("api/autos")
    suspend fun createAuto(@Body auto: Auto): Response<Auto>

    @PUT("api/autos/{id}")
    suspend fun updateAuto(@Path("id") id: Long, @Body auto: Auto): Response<Auto>

    @DELETE("api/autos/{id}")
    suspend fun deleteAuto(@Path("id") id: Long): Response<Unit>

    @GET("api/usuarios")
    suspend fun getUsers(): List<Usuario>

    @DELETE("api/usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Unit>

    @POST("api/usuarios/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Any>

    @POST("api/usuarios/login")
    suspend fun login(@Body loginData: Map<String, String>): Response<Usuario>
}