package com.juni.mensajepush.utilidades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juni.mensajepush.presentacion.autenticacion.AutenticacionViewModel
import com.juni.mensajepush.presentacion.conversacion.ConversacionViewModel

class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AutenticacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AutenticacionViewModel(
                appContainer.iniciarSesionUseCase,
                appContainer.registrarDispositivoUseCase
            ) as T
        }
        if (modelClass.isAssignableFrom(ConversacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversacionViewModel(
                appContainer.obtenerMensajesUseCase,
                appContainer.enviarMensajeUseCase,
                appContainer.marcarLeidoUseCase,
                appContainer.editarMensajeUseCase,
                appContainer.eliminarMensajeUseCase
            ) as T
        }
        if (modelClass.isAssignableFrom(com.juni.mensajepush.presentacion.vinculacion.VinculacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.juni.mensajepush.presentacion.vinculacion.VinculacionViewModel(appContainer.vincularUsuarioUseCase) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
