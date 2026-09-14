package com.juni.mensajepush.dominio.repositorios

import com.juni.mensajepush.dominio.modelos.Mensaje

interface RepositorioMensajes {
    suspend fun obtenerMensajes(): Result<List<Mensaje>>
    suspend fun enviarMensaje(contenido: String, destinatarioId: String): Result<Mensaje>
}
