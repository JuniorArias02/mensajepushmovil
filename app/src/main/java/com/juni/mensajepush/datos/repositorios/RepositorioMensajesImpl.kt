package com.juni.mensajepush.datos.repositorios

import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.remoto.api.EnviarMensajeRequest
import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioAutenticacion
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositorioMensajesImpl(
    private val api: ApiMensajeria,
    private val repositorioAutenticacion: RepositorioAutenticacion
) : RepositorioMensajes {

    override suspend fun obtenerMensajes(): Result<List<Mensaje>> {
        return withContext(Dispatchers.IO) {
            try {
                val miUsuario = repositorioAutenticacion.obtenerUsuarioActual()
                    ?: return@withContext Result.failure(Exception("No autenticado"))
                    
                val respuesta = api.obtenerMensajes()
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val mensajes = respuesta.body()!!.map { it.aDominio(miUsuario.id) }
                    Result.success(mensajes)
                } else {
                    Result.failure(Exception("Error al obtener mensajes: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun enviarMensaje(contenido: String, destinatarioId: String): Result<Mensaje> {
        return withContext(Dispatchers.IO) {
            try {
                val miUsuario = repositorioAutenticacion.obtenerUsuarioActual()
                    ?: return@withContext Result.failure(Exception("No autenticado"))

                val respuesta = api.enviarMensaje(EnviarMensajeRequest(contenido, destinatarioId))
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    Result.success(respuesta.body()!!.aDominio(miUsuario.id))
                } else {
                    Result.failure(Exception("Error al enviar mensaje: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
