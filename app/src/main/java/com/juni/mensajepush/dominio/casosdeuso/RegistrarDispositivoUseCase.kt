package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.remoto.api.PeticionDispositivo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegistrarDispositivoUseCase(private val api: ApiMensajeria) {
    suspend operator fun invoke(tokenFcm: String, nombreDispositivo: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = api.registrarDispositivo(
                    PeticionDispositivo(
                        token_fcm = tokenFcm,
                        nombre_dispositivo = nombreDispositivo,
                        plataforma = "android"
                    )
                )
                if (respuesta.isSuccessful && respuesta.body()?.exito == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(respuesta.body()?.mensaje ?: "Error desconocido"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
