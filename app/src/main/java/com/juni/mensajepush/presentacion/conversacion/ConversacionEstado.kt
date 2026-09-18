package com.juni.mensajepush.presentacion.conversacion

import com.juni.mensajepush.dominio.modelos.Mensaje

sealed class ConversacionEstado {
    object Cargando : ConversacionEstado()
    data class Exito(val mensajes: List<Mensaje>) : ConversacionEstado()
    data class Error(val mensaje: String) : ConversacionEstado()
    object Vacio : ConversacionEstado()
    object SinVinculacion : ConversacionEstado()
}
