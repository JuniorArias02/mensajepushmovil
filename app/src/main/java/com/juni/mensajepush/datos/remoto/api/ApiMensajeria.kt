package com.juni.mensajepush.datos.remoto.api

import com.juni.mensajepush.datos.remoto.dto.MensajeDto
import com.juni.mensajepush.datos.remoto.dto.RespuestaBase
import com.juni.mensajepush.datos.remoto.dto.RespuestaLogin
import com.juni.mensajepush.datos.remoto.dto.RespuestaCodigo
import com.juni.mensajepush.datos.remoto.dto.RespuestaVinculacion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// --- Peticiones (Requests) ---
data class PeticionIniciarSesion(val correo: String, val contrasena: String)
data class PeticionDispositivo(val token_fcm: String, val plataforma: String = "android", val nombre_dispositivo: String)
data class PeticionVincularCorreo(val correo: String)
data class PeticionMensaje(val contenido: String, val respuesta_a_id: String? = null)

// --- Interfaz Retrofit ---
interface ApiMensajeria {

    // 2.1 Autenticación
    @POST("autenticacion/iniciar-sesion")
    suspend fun iniciarSesion(@Body request: PeticionIniciarSesion): Response<RespuestaBase<RespuestaLogin>>

    @POST("autenticacion/cerrar-sesion")
    suspend fun cerrarSesion(): Response<RespuestaBase<Any>>

    // 2.2 Notificaciones Push
    @POST("dispositivos")
    suspend fun registrarDispositivo(@Body request: PeticionDispositivo): Response<RespuestaBase<Any>>

    // 2.3 Sistema de Vinculación
    @POST("vinculaciones/vincular-correo")
    suspend fun vincularPorCorreo(@Body request: PeticionVincularCorreo): Response<RespuestaBase<RespuestaVinculacion>>

    // 2.4 Chat y Mensajes
    @GET("mensajes")
    suspend fun obtenerMensajes(): Response<RespuestaBase<List<MensajeDto>>>

    @retrofit2.http.Multipart
    @POST("mensajes")
    suspend fun enviarMensaje(
        @retrofit2.http.Part("contenido") contenido: okhttp3.RequestBody?,
        @retrofit2.http.Part("respuesta_a_id") respuestaAId: okhttp3.RequestBody?,
        @retrofit2.http.Part media: okhttp3.MultipartBody.Part?
    ): Response<RespuestaBase<MensajeDto>>
    
    @PATCH("mensajes/{id}/leer")
    suspend fun marcarComoLeido(@Path("id") id: String): Response<RespuestaBase<Any>>
    
    @retrofit2.http.PUT("mensajes/{id}")
    suspend fun editarMensaje(@Path("id") id: String, @Body request: PeticionMensaje): Response<RespuestaBase<MensajeDto>>

    @retrofit2.http.DELETE("mensajes/{id}")
    suspend fun eliminarMensaje(@Path("id") id: String): Response<RespuestaBase<MensajeDto>>
}
