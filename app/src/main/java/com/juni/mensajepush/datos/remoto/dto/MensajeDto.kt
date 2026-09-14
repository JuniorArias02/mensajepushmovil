package com.juni.mensajepush.datos.remoto.dto

import com.juni.mensajepush.dominio.modelos.Mensaje

data class MensajeDto(
    val id: String,
    val sender_id: String,
    val receiver_id: String,
    val content: String,
    val created_at: String
) {
    fun aDominio(miUsuarioId: String): Mensaje {
        return Mensaje(
            id = id,
            remitenteId = sender_id,
            destinatarioId = receiver_id,
            contenido = content,
            fechaEnvio = created_at,
            esMio = sender_id == miUsuarioId
        )
    }
}
