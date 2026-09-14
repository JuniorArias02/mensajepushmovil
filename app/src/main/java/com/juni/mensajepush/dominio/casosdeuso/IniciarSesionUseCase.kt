package com.juni.mensajepush.dominio.casosdeuso

import com.juni.mensajepush.dominio.modelos.Usuario
import com.juni.mensajepush.dominio.repositorios.RepositorioAutenticacion

class IniciarSesionUseCase(private val repositorio: RepositorioAutenticacion) {
    suspend operator fun invoke(correo: String, contrasena: String): Result<Usuario> {
        if (correo.isBlank() || contrasena.isBlank()) {
            return Result.failure(Exception("El correo y la contraseña no pueden estar vacíos."))
        }
        return repositorio.iniciarSesion(correo, contrasena)
    }
}
