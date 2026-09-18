package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes

class EnviarMensajeUseCase(private val repositorio: RepositorioMensajes) {
    suspend operator fun invoke(contenido: String? = null, destinatarioId: String, respuestaAId: String? = null, mediaFile: java.io.File? = null): Result<Mensaje> {
        if (contenido.isNullOrBlank() && mediaFile == null) {
            return Result.failure(Exception("El mensaje no puede estar vacío si no hay archivo adjunto."))
        }
        return repositorio.enviarMensaje(contenido, destinatarioId, respuestaAId, mediaFile)
    }
}
