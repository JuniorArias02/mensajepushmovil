package com.juni.mensajepush.presentacion.autenticacion

sealed class AutenticacionEstado {
    object Inicial : AutenticacionEstado()
    object Cargando : AutenticacionEstado()
    object Exito : AutenticacionEstado()
    data class Error(val mensaje: String) : AutenticacionEstado()
}
