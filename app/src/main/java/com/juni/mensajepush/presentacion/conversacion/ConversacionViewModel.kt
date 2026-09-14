package com.juni.mensajepush.presentacion.conversacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juni.mensajepush.dominio.casosdeuso.EnviarMensajeUseCase
import com.juni.mensajepush.dominio.casosdeuso.ObtenerMensajesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConversacionViewModel(
    private val obtenerMensajesUseCase: ObtenerMensajesUseCase,
    private val enviarMensajeUseCase: EnviarMensajeUseCase
) : ViewModel() {

    private val _estado = MutableStateFlow<ConversacionEstado>(ConversacionEstado.Cargando)
    val estado: StateFlow<ConversacionEstado> = _estado.asStateFlow()
    
    private val _enviando = MutableStateFlow(false)
    val enviando: StateFlow<Boolean> = _enviando.asStateFlow()

    private var pollingJob: kotlinx.coroutines.Job? = null

    init {
        iniciarPolling()
    }

    private fun iniciarPolling() {
        pollingJob = viewModelScope.launch {
            while (true) {
                cargarMensajes()
                delay(5000) // Polling cada 5 segundos
            }
        }
    }

    private suspend fun cargarMensajes() {
        val resultado = obtenerMensajesUseCase()
        if (resultado.isSuccess) {
            val mensajes = resultado.getOrNull() ?: emptyList()
            if (mensajes.isEmpty()) {
                _estado.value = ConversacionEstado.Vacio
            } else {
                _estado.value = ConversacionEstado.Exito(mensajes)
            }
        } else {
            if (_estado.value !is ConversacionEstado.Exito) {
                _estado.value = ConversacionEstado.Error("Error al cargar mensajes")
            }
        }
    }

    fun enviarMensaje(contenido: String, destinatarioId: String) {
        if (contenido.isBlank()) return
        
        viewModelScope.launch {
            _enviando.value = true
            val resultado = enviarMensajeUseCase(contenido, destinatarioId)
            _enviando.value = false
            
            if (resultado.isSuccess) {
                cargarMensajes() // Recargar inmediatamente al enviar
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
    }
}
