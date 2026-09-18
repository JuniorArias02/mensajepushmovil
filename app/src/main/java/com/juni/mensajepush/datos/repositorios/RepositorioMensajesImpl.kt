package com.juni.mensajepush.datos.repositorios

import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.remoto.api.PeticionMensaje
import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioAutenticacion
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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
                    val cuerpo = respuesta.body()!!
                    if (cuerpo.exito && cuerpo.datos != null) {
                        val mensajes = cuerpo.datos.map { it.aDominio(miUsuario.id) }
                        Result.success(mensajes)
                    } else {
                        Result.failure(Exception(cuerpo.mensaje))
                    }
                } else {
                    if (respuesta.code() == 403) {
                        Result.failure(Exception("SinVinculacion"))
                    } else {
                        Result.failure(Exception("Error al obtener mensajes: ${respuesta.code()}"))
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun enviarMensaje(contenido: String?, destinatarioId: String, respuestaAId: String?, mediaFile: java.io.File?): Result<Mensaje> {
        return withContext(Dispatchers.IO) {
            try {
                val miUsuario = repositorioAutenticacion.obtenerUsuarioActual()
                    ?: return@withContext Result.failure(Exception("No autenticado"))

                val contenidoBody = contenido?.toRequestBody("text/plain".toMediaTypeOrNull())
                val respuestaABody = respuestaAId?.toRequestBody("text/plain".toMediaTypeOrNull())

                val mediaPart = mediaFile?.let {
                    val requestFile = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    okhttp3.MultipartBody.Part.createFormData("media", it.name, requestFile)
                }

                val respuesta = api.enviarMensaje(contenidoBody, respuestaABody, mediaPart)
                
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val cuerpo = respuesta.body()!!
                    if (cuerpo.exito && cuerpo.datos != null) {
                        Result.success(cuerpo.datos.aDominio(miUsuario.id))
                    } else {
                        Result.failure(Exception(cuerpo.mensaje))
                    }
                } else {
                    Result.failure(Exception("Error al enviar mensaje: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun marcarComoLeido(id: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = api.marcarComoLeido(id)
                if (respuesta.isSuccessful && respuesta.body()?.exito == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al marcar como leído"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    override suspend fun editarMensaje(id: String, contenido: String): Result<Mensaje> {
        return withContext(Dispatchers.IO) {
            try {
                val miUsuario = repositorioAutenticacion.obtenerUsuarioActual()
                    ?: return@withContext Result.failure(Exception("No autenticado"))

                val respuesta = api.editarMensaje(id, PeticionMensaje(contenido))
                
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val cuerpo = respuesta.body()!!
                    if (cuerpo.exito && cuerpo.datos != null) {
                        Result.success(cuerpo.datos.aDominio(miUsuario.id))
                    } else {
                        Result.failure(Exception(cuerpo.mensaje))
                    }
                } else {
                    Result.failure(Exception("Error al editar mensaje: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun eliminarMensaje(id: String): Result<Mensaje> {
        return withContext(Dispatchers.IO) {
            try {
                val miUsuario = repositorioAutenticacion.obtenerUsuarioActual()
                    ?: return@withContext Result.failure(Exception("No autenticado"))

                val respuesta = api.eliminarMensaje(id)
                
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val cuerpo = respuesta.body()!!
                    if (cuerpo.exito && cuerpo.datos != null) {
                        Result.success(cuerpo.datos.aDominio(miUsuario.id))
                    } else {
                        Result.failure(Exception(cuerpo.mensaje))
                    }
                } else {
                    Result.failure(Exception("Error al eliminar mensaje: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
