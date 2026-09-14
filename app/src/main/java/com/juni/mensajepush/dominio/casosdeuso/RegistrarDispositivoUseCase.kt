package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.remoto.api.DispositivoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegistrarDispositivoUseCase(private val api: ApiMensajeria) {
    suspend operator fun invoke(tokenFcm: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = api.registrarDispositivo(DispositivoRequest(tokenFcm))
                if (respuesta.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al registrar dispositivo"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
