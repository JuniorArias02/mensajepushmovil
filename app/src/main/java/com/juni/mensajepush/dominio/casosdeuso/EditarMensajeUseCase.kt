package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes

class EditarMensajeUseCase(private val repositorioMensajes: RepositorioMensajes) {
    suspend operator fun invoke(id: String, contenido: String): Result<Mensaje> {
        return repositorioMensajes.editarMensaje(id, contenido)
    }
}
