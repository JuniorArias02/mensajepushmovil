package com.juni.mensajepush.utilidades

import android.content.Context
import com.juni.mensajepush.datos.remoto.api.ApiMensajeria
import com.juni.mensajepush.datos.repositorios.RepositorioAutenticacionImpl
import com.juni.mensajepush.datos.repositorios.RepositorioMensajesImpl
import com.juni.mensajepush.dominio.casosdeuso.EnviarMensajeUseCase
import com.juni.mensajepush.dominio.casosdeuso.IniciarSesionUseCase
import com.juni.mensajepush.dominio.casosdeuso.ObtenerMensajesUseCase
import com.juni.mensajepush.dominio.casosdeuso.RegistrarDispositivoUseCase
import com.juni.mensajepush.dominio.repositorios.RepositorioAutenticacion
import com.juni.mensajepush.dominio.repositorios.RepositorioMensajes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor

class AppContainer(private val context: Context) {

    // URL de Laravel. (Para el emulador de Android Studio usar 10.0.2.2 en lugar de localhost)
    private val BASE_URL = "http://10.0.2.2:8000/api/" 

    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        val prefs = context.getSharedPreferences("mensajeria_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: ApiMensajeria = retrofit.create(ApiMensajeria::class.java)

    // Repositorios
    val repositorioAutenticacion: RepositorioAutenticacion by lazy {
        RepositorioAutenticacionImpl(api, context)
    }

    val repositorioMensajes: RepositorioMensajes by lazy {
        RepositorioMensajesImpl(api, repositorioAutenticacion)
    }

    // Casos de Uso
    val iniciarSesionUseCase: IniciarSesionUseCase by lazy {
        IniciarSesionUseCase(repositorioAutenticacion)
    }

    val obtenerMensajesUseCase: ObtenerMensajesUseCase by lazy {
        ObtenerMensajesUseCase(repositorioMensajes)
    }

    val enviarMensajeUseCase: EnviarMensajeUseCase by lazy {
        EnviarMensajeUseCase(repositorioMensajes)
    }
    
    val registrarDispositivoUseCase: RegistrarDispositivoUseCase by lazy {
        RegistrarDispositivoUseCase(api)
    }
}
