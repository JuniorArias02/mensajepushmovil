package com.juni.mensajepush.datos.remoto.api

import com.juni.mensajepush.datos.remoto.dto.MensajeDto
import com.juni.mensajepush.datos.remoto.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class EnviarMensajeRequest(val content: String, val receiver_id: String)
data class DispositivoRequest(val token: String)

interface ApiMensajeria {
    @POST("login")
    suspend fun iniciarSesion(@Body request: LoginRequest): Response<UsuarioDto>

    @POST("logout")
    suspend fun cerrarSesion(): Response<Unit>

    @GET("mensajes")
    suspend fun obtenerMensajes(): Response<List<MensajeDto>>

    @POST("mensajes")
    suspend fun enviarMensaje(@Body request: EnviarMensajeRequest): Response<MensajeDto>
    
    @POST("dispositivos")
    suspend fun registrarDispositivo(@Body request: DispositivoRequest): Response<Unit>
}
