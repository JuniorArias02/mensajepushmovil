package com.juni.mensajepush.datos.repositorios

import android.content.Context
import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.remoto.api.PeticionIniciarSesion
import com.juni.mensajepush.dominio.modelos.Usuario
import com.juni.mensajepush.dominio.repositorios.RepositorioAutenticacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositorioAutenticacionImpl(
    private val api: ApiMensajeria,
    private val context: Context
) : RepositorioAutenticacion {

    private val prefs = context.getSharedPreferences("mensajeria_prefs", Context.MODE_PRIVATE)

    override suspend fun iniciarSesion(correo: String, contrasena: String): Result<Usuario> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = api.iniciarSesion(PeticionIniciarSesion(correo, contrasena))
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val cuerpo = respuesta.body()!!
                    if (cuerpo.exito && cuerpo.datos != null) {
                        val dto = cuerpo.datos
                        // Guardar token
                        prefs.edit().putString("auth_token", dto.token).apply()
                        
                        // Guardar usuario actual
                        prefs.edit().putString("user_id", dto.usuario.id).apply()
                        prefs.edit().putString("user_name", dto.usuario.nombre).apply()
                        prefs.edit().putString("user_email", dto.usuario.correo).apply()

                        Result.success(dto.usuario.aDominio())
                    } else {
                        Result.failure(Exception(cuerpo.mensaje))
                    }
                } else {
                    Result.failure(Exception("Error al iniciar sesión: ${respuesta.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun cerrarSesion(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                api.cerrarSesion()
                prefs.edit().clear().apply()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun obtenerUsuarioActual(): Usuario? {
        val id = prefs.getString("user_id", null) ?: return null
        val nombre = prefs.getString("user_name", "") ?: ""
        val correo = prefs.getString("user_email", "") ?: ""
        return Usuario(id, nombre, correo)
    }
}
