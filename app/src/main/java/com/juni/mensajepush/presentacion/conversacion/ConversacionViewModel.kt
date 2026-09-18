package com.juni.mensajepush.presentacion.conversacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juni.mensajepush.dominio.casosdeuso.EnviarMensajeUseCase
import com.juni.mensajepush.dominio.casosdeuso.ObtenerMensajesUseCase
import com.juni.mensajepush.dominio.casosdeuso.MarcarLeidoUseCase
import com.juni.mensajepush.dominio.modelos.Mensaje
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConversacionViewModel(
    private val obtenerMensajesUseCase: ObtenerMensajesUseCase,
    private val enviarMensajeUseCase: EnviarMensajeUseCase,
    private val marcarLeidoUseCase: MarcarLeidoUseCase,
    private val editarMensajeUseCase: com.juni.mensajepush.dominio.casosdeuso.EditarMensajeUseCase,
    private val eliminarMensajeUseCase: com.juni.mensajepush.dominio.casosdeuso.EliminarMensajeUseCase
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
                
                // Marcar como leídos los mensajes que no son míos y no están leídos
                mensajes.filter { !it.esMio && !it.leido }.forEach { mensajeNoLeido ->
                    viewModelScope.launch {
                        marcarLeidoUseCase(mensajeNoLeido.id)
                    }
                }
            }
        } else {
            val exceptionMessage = resultado.exceptionOrNull()?.message
            if (exceptionMessage == "SinVinculacion") {
                _estado.value = ConversacionEstado.SinVinculacion
            } else if (_estado.value !is ConversacionEstado.Exito) {
                _estado.value = ConversacionEstado.Error("Error al cargar mensajes")
            }
        }
    }

    private val _mensajeRespuesta = MutableStateFlow<Mensaje?>(null)
    val mensajeRespuesta: StateFlow<Mensaje?> = _mensajeRespuesta.asStateFlow()

    fun iniciarRespuesta(mensaje: Mensaje) {
        _mensajeRespuesta.value = mensaje
    }

    fun cancelarRespuesta() {
        _mensajeRespuesta.value = null
    }

    fun enviarMensaje(contenido: String, mediaFile: java.io.File? = null) {
        if (contenido.isBlank() && mediaFile == null) return
        
        viewModelScope.launch {
            _enviando.value = true
            val idRespuesta = _mensajeRespuesta.value?.id
            _mensajeRespuesta.value = null // Limpiar la UI inmediatamente
            
            val resultado = enviarMensajeUseCase(contenido.ifBlank { null }, "", idRespuesta, mediaFile)
            _enviando.value = false
            
            if (resultado.isSuccess) {
                cargarMensajes() // Recargar inmediatamente al enviar
            }
        }
    }

    fun editarMensaje(id: String, contenido: String) {
        if (contenido.isBlank()) return
        
        viewModelScope.launch {
            _enviando.value = true
            val resultado = editarMensajeUseCase(id, contenido)
            _enviando.value = false
            
            if (resultado.isSuccess) {
                cargarMensajes()
            }
        }
    }

    fun eliminarMensaje(id: String) {
        viewModelScope.launch {
            _enviando.value = true
            val resultado = eliminarMensajeUseCase(id)
            _enviando.value = false
            
            if (resultado.isSuccess) {
                cargarMensajes()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
    }
}
