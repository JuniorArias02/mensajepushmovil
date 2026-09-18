package com.juni.mensajepush.datos.remoto.dto

data class RespuestaBase<T>(
    val exito: Boolean,
    val mensaje: String,
    val datos: T?
)
