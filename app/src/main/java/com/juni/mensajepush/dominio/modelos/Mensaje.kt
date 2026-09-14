package com.juni.mensajepush.dominio.modelos

data class Mensaje(
    val id: String,
    val remitenteId: String,
    val destinatarioId: String,
    val contenido: String,
    val fechaEnvio: String,
    val esMio: Boolean
)
