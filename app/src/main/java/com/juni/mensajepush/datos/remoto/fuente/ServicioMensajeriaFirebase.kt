package com.juni.mensajepush.datos.remoto.fuente

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.juni.mensajepush.utilidades.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServicioMensajeriaFirebase : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")
        
        // Enviar a Laravel
        val appContainer = AppContainer(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            appContainer.registrarDispositivoUseCase(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Mensaje recibido de: ${message.from}")

        // El polling de la app actualizará la UI si está abierta.
        // Si la app está en segundo plano y envías un payload "notification",
        // el sistema operativo Android mostrará la notificación automáticamente.
    }
}
