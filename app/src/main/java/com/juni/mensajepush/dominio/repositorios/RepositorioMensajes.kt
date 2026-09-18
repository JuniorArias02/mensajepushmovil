package com.juni.mensajepush.dominio.repositorios

import com.juni.mensajepush.dominio.modelos.Mensaje

interface RepositorioMensajes {
    suspend fun obtenerMensajes(): Result<List<Mensaje>>
    suspend fun enviarMensaje(contenido: String?, destinatarioId: String, respuestaAId: String? = null, mediaFile: java.io.File? = null): Result<Mensaje>
    suspend fun marcarComoLeido(id: String): Result<Unit>
    suspend fun editarMensaje(id: String, contenido: String): Result<Mensaje>
    suspend fun eliminarMensaje(id: String): Result<Mensaje>
}
