package com.juni.mensajepush.dominio.repositorios

import com.juni.mensajepush.dominio.modelos.Usuario

interface RepositorioAutenticacion {
    suspend fun iniciarSesion(correo: String, contrasena: String): Result<Usuario>
    suspend fun cerrarSesion(): Result<Unit>
    fun obtenerUsuarioActual(): Usuario?
}
