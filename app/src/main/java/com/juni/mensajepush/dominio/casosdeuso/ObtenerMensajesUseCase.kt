package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.modelos.Mensaje
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes

class ObtenerMensajesUseCase(private val repositorio: RepositorioMensajes) {
    suspend operator fun invoke(): Result<List<Mensaje>> {
        return repositorio.obtenerMensajes()
    }
}
