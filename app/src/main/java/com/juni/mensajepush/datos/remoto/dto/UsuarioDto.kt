package com.juni.mensajepush.datos.remoto.dto

import com.juni.mensajepush.dominio.modelos.Usuario

data class UsuarioDto(
    val id: String,
    val nombre: String,
    val correo: String,
    val creado_en: String? = null
) {
    fun aDominio(): Usuario {
        return Usuario(
            id = id,
            nombre = nombre,
            correo = correo
        )
    }
}

data class RespuestaLogin(
    val token: String,
    val usuario: UsuarioDto
)

data class RespuestaCodigo(
    val codigo: String,
    val expira_en: String
)

data class RespuestaVinculacion(
    val vinculacion_id: String
)
