package com.juni.mensajepush.dominio.modelos

data class Mensaje(
    val id: String,
    val remitenteId: String,
    val destinatarioId: String,
    val contenido: String,
    val fechaEnvio: String,
    val esMio: Boolean,
    val leido: Boolean = false,
    val entregado: Boolean = false,
    val respuestaAMensaje: Mensaje? = null,
    val editadoEn: String? = null,
    val eliminadoEn: String? = null,
    val mediaUrl: String? = null,
    val mediaType: String? = null
)
