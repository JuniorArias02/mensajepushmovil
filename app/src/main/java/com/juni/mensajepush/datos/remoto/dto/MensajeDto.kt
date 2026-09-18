package com.juni.mensajepush.datos.remoto.dto

import com.juni.mensajepush.dominio.modelos.Mensaje

data class MensajeDto(
    val id: String,
    val vinculacion_id: String,
    val remitente_id: String,
    val contenido: String,
    val leido_en: String?,
    val entregado_en: String?,
    val creado_en: String,
    val editado_en: String? = null,
    val eliminado_en: String? = null,
    val respuesta_a: MensajeDto? = null,
    val media_url: String? = null,
    val media_type: String? = null
) {
    fun aDominio(miUsuarioId: String): Mensaje {
        return Mensaje(
            id = id,
            remitenteId = remitente_id,
            destinatarioId = "", // En esta nueva API el destinatario se infiere
            contenido = contenido,
            fechaEnvio = creado_en,
            esMio = remitente_id == miUsuarioId,
            leido = leido_en != null,
            entregado = entregado_en != null,
            respuestaAMensaje = respuesta_a?.aDominio(miUsuarioId),
            editadoEn = editado_en,
            eliminadoEn = eliminado_en,
            mediaUrl = media_url,
            mediaType = media_type
        )
    }
}
