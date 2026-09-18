package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes

class EliminarMensajeUseCase(private val repositorioMensajes: RepositorioMensajes) {
    suspend operator fun invoke(id: String): Result<Mensaje> {
        return repositorioMensajes.eliminarMensaje(id)
    }
}
