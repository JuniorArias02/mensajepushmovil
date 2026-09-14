package com.juni.mensajepush.datos.remoto.dto

import com.juni.mensajepush.dominio.modelos.Usuario

data class UsuarioDto(
    val id: String,
    val name: String,
    val email: String,
    val token: String? = null
) {
    fun aDominio(): Usuario {
        return Usuario(
            id = id,
            nombre = name,
            correo = email
        )
    }
}
