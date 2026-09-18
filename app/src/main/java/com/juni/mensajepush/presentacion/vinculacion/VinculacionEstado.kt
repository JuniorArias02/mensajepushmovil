package com.juni.mensajepush.presentacion.vinculacion

sealed class VinculacionEstado {
    object Inicial : VinculacionEstado()
    object Cargando : VinculacionEstado()
    object Exito : VinculacionEstado()
    data class Error(val mensaje: String) : VinculacionEstado()
}
