package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.remoto.api.PeticionVincularCorreo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VincularUsuarioUseCase(private val api: ApiMensajeria) {
    suspend operator fun invoke(correo: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = api.vincularPorCorreo(PeticionVincularCorreo(correo))
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val cuerpo = respuesta.body()!!
                    if (cuerpo.exito && cuerpo.datos != null) {
                        Result.success(cuerpo.datos.vinculacion_id)
                    } else {
                        Result.failure(Exception(cuerpo.mensaje))
                    }
                } else {
                    Result.failure(Exception("Error al vincular: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
