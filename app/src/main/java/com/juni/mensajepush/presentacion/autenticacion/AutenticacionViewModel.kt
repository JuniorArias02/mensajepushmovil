package com.juni.mensajepush.presentacion.autenticacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juni.mensajepush.dominio.casosdeuso.IniciarSesionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AutenticacionViewModel(
    private val iniciarSesionUseCase: IniciarSesionUseCase
) : ViewModel() {

    private val _estado = MutableStateFlow<AutenticacionEstado>(AutenticacionEstado.Inicial)
    val estado: StateFlow<AutenticacionEstado> = _estado.asStateFlow()

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            _estado.value = AutenticacionEstado.Cargando
            val resultado = iniciarSesionUseCase(correo, contrasena)
            if (resultado.isSuccess) {
                _estado.value = AutenticacionEstado.Exito
            } else {
                val error = resultado.exceptionOrNull()?.message ?: "Error desconocido"
                _estado.value = AutenticacionEstado.Error(error)
            }
        }
    }
    
    fun reiniciarEstado() {
        _estado.value = AutenticacionEstado.Inicial
    }
}
