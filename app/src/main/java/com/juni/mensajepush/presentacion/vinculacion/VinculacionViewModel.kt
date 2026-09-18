package com.juni.mensajepush.presentacion.vinculacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juni.mensajepush.dominio.casosdeuso.VincularUsuarioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VinculacionViewModel(
    private val vincularUsuarioUseCase: VincularUsuarioUseCase
) : ViewModel() {

    private val _estado = MutableStateFlow<VinculacionEstado>(VinculacionEstado.Inicial)
    val estado: StateFlow<VinculacionEstado> = _estado.asStateFlow()

    fun vincularPorCorreo(correo: String) {
        if (correo.isBlank()) {
            _estado.value = VinculacionEstado.Error("El correo no puede estar vacío")
            return
        }

        _estado.value = VinculacionEstado.Cargando

        viewModelScope.launch {
            val resultado = vincularUsuarioUseCase(correo)
            if (resultado.isSuccess) {
                _estado.value = VinculacionEstado.Exito
            } else {
                _estado.value = VinculacionEstado.Error(
                    resultado.exceptionOrNull()?.message ?: "Error desconocido"
                )
            }
        }
    }
}
