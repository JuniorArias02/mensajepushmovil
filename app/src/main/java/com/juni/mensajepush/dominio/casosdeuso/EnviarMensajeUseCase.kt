package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes

class EnviarMensajeUseCase(private val repositorio: RepositorioMensajes) {
    suspend operator fun invoke(contenido: String, destinatarioId: String): Result<Mensaje> {
        if (contenido.isBlank()) {
            return Result.failure(Exception("El mensaje no puede estar vacío."))
        }
        return repositorio.enviarMensaje(contenido, destinatarioId)
    }
}
