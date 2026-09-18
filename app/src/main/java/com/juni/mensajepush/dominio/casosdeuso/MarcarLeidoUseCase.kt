package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes

class MarcarLeidoUseCase(private val repositorio: RepositorioMensajes) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repositorio.marcarComoLeido(id)
    }
}
