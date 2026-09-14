package com.juni.mensajepush.utilidades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juni.mensajepush.presentacion.autenticacion.AutenticacionViewModel
import com.juni.mensajepush.presentacion.conversacion.ConversacionViewModel

class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AutenticacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AutenticacionViewModel(appContainer.iniciarSesionUseCase) as T
        }
        if (modelClass.isAssignableFrom(ConversacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversacionViewModel(
                appContainer.obtenerMensajesUseCase,
                appContainer.enviarMensajeUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
