# Reglas de desarrollo — Aplicación móvil

## 1. Objetivo

Construir una aplicación Android privada de mensajería entre dos personas.

Tecnología principal:

- Kotlin
- Android
- Clean Architecture
- MVVM
- Retrofit
- Coroutines
- Firebase Cloud Messaging
- Jetpack

La aplicación se comunicará exclusivamente con la API Laravel.

---

# 2. Arquitectura

La aplicación debe utilizar Clean Architecture.

Capas:

presentation
domain
data

Flujo:

Interfaz
    ↓
ViewModel
    ↓
Caso de uso
    ↓
Repositorio
    ↓
Fuente de datos
    ↓
Retrofit
    ↓
Laravel API

Las capas internas no deben depender directamente de Android o Retrofit cuando no sea necesario.

---

# 3. Idioma obligatorio

Todo el código propio de la aplicación debe estar en español.

Esto incluye:

- Carpetas.
- Clases.
- Interfaces.
- Métodos.
- Variables.
- Casos de uso.
- Modelos.
- DTO.
- Estados.
- Eventos.
- Recursos.
- Textos de interfaz.
- Mensajes.

Ejemplo:

Correcto:

IniciarSesionUseCase
EnviarMensajeUseCase
ObtenerMensajesUseCase
RegistrarDispositivoUseCase

Incorrecto:

LoginUseCase
SendMessageUseCase
GetMessagesUseCase

No mezclar español e inglés.

Las APIs propias de Android/Kotlin pueden conservar sus nombres oficiales.

---

# 4. Estructura

Ejemplo:

app/
└── src/
    └── main/
        ├── java/
        │   └── com/aplicacion/mensajeria/
        │       │
        │       ├── presentacion/
        │       │   ├── autenticacion/
        │       │   ├── conversacion/
        │       │   ├── vinculacion/
        │       │   └── perfil/
        │       │
        │       ├── dominio/
        │       │   ├── modelos/
        │       │   ├── repositorios/
        │       │   └── casosdeuso/
        │       │
        │       ├── datos/
        │       │   ├── remoto/
        │       │   │   ├── api/
        │       │   │   ├── dto/
        │       │   │   └── fuente/
        │       │   │
        │       │   ├── local/
        │       │   └── repositorios/
        │       │
        │       └── utilidades/
        │
        └── res/

---

# 5. Presentación

La capa de presentación contiene:

- Pantallas.
- ViewModels.
- Estados.
- Eventos de UI.
- Componentes visuales.

Ejemplo:

presentacion/
└── conversacion/
    ├── ConversacionPantalla.kt
    ├── ConversacionViewModel.kt
    ├── ConversacionEstado.kt
    └── ConversacionEvento.kt

El ViewModel NO debe realizar directamente peticiones HTTP.

Incorrecto:

ViewModel
    ↓
Retrofit

Correcto:

ViewModel
    ↓
CasoDeUso
    ↓
Repositorio
    ↓
FuenteRemota
    ↓
Retrofit

---

# 6. Dominio

La capa de dominio contiene las reglas principales de la aplicación.

Ejemplo:

dominio/
├── modelos/
│   ├── Usuario.kt
│   ├── Mensaje.kt
│   ├── Dispositivo.kt
│   └── Vinculacion.kt
│
├── repositorios/
│   ├── RepositorioAutenticacion.kt
│   ├── RepositorioMensajes.kt
│   └── RepositorioDispositivos.kt
│
└── casosdeuso/
    ├── IniciarSesionUseCase.kt
    ├── CerrarSesionUseCase.kt
    ├── EnviarMensajeUseCase.kt
    ├── ObtenerMensajesUseCase.kt
    ├── MarcarMensajeLeidoUseCase.kt
    ├── GenerarCodigoVinculacionUseCase.kt
    └── VincularUsuarioUseCase.kt

La capa de dominio no debe depender directamente de Retrofit, Android o Firebase.

---

# 7. Datos

La capa de datos implementa las interfaces definidas en dominio.

Ejemplo:

datos/
├── remoto/
│   ├── api/
│   │   └── ApiMensajeria.kt
│   │
│   ├── dto/
│   │   ├── UsuarioDto.kt
│   │   ├── MensajeDto.kt
│   │   └── RespuestaApiDto.kt
│   │
│   └── fuente/
│       └── FuenteRemotaMensajeria.kt
│
└── repositorios/
    ├── RepositorioAutenticacionImpl.kt
    └── RepositorioMensajesImpl.kt

---

# 8. Retrofit

Retrofit será utilizado exclusivamente en la capa de datos.

Ejemplo:

ApiMensajeria.kt

Debe contener las operaciones HTTP.

No colocar lógica de negocio dentro de la interfaz Retrofit.

---

# 9. Autenticación

Flujo:

Usuario
 ↓
Pantalla de inicio de sesión
 ↓
ViewModel
 ↓
IniciarSesionUseCase
 ↓
Repositorio
 ↓
Retrofit
 ↓
Laravel
 ↓
Token
 ↓
Almacenamiento local seguro

El token debe almacenarse de forma segura.

No guardar tokens en texto plano dentro de preferencias normales si existe una alternativa segura disponible.

---

# 10. Interceptor

Retrofit debe utilizar un interceptor para agregar:

Authorization: Bearer TOKEN

De esta forma no será necesario agregar manualmente el token a cada petición.

---

# 11. Firebase Cloud Messaging

Firebase FCM será utilizado para recibir notificaciones push.

Cuando la aplicación obtenga un token FCM:

Firebase
    ↓
Token
    ↓
Aplicación
    ↓
Laravel

La aplicación debe registrar o actualizar el token mediante la API.

Endpoint:

POST /api/dispositivos

---

# 12. Recepción de mensajes push

Cuando llegue una notificación:

Firebase
    ↓
Servicio de mensajería Firebase
    ↓
Aplicación

La notificación debe utilizarse para informar que existe un nuevo mensaje.

El mensaje completo debe consultarse desde Laravel cuando sea necesario.

No utilizar la notificación como base de datos.

---

# 13. Conversación

La pantalla principal será la conversación.

Debe permitir:

- Ver mensajes.
- Enviar mensajes.
- Mostrar mensajes propios.
- Mostrar mensajes recibidos.
- Mostrar estado de lectura.
- Mostrar fecha/hora.
- Actualizar mensajes nuevos.

Los mensajes deben diferenciarse visualmente según el remitente.

---

# 14. Envío de mensajes

Flujo:

Usuario escribe
    ↓
ViewModel
    ↓
EnviarMensajeUseCase
    ↓
Repositorio
    ↓
Retrofit
    ↓
Laravel
    ↓
MySQL
    ↓
FCM
    ↓
Usuario destinatario

El móvil no debe decidir quién es el remitente.

Laravel determina el remitente mediante autenticación.

---

# 15. Actualización de mensajes

Inicialmente se utilizará polling mientras la conversación esté abierta.

Ejemplo:

GET /api/mensajes

El intervalo debe ser razonable para evitar solicitudes innecesarias.

Posteriormente se puede implementar WebSocket si el proyecto realmente lo requiere.

---

# 16. Estado de la aplicación

La UI debe representar estados claros.

Ejemplo:

Cargando
Exitoso
Error
Sin mensajes
Enviando
Mensaje enviado

Evitar múltiples variables booleanas independientes cuando un estado sellado pueda representar mejor la situación.

Ejemplo conceptual:

EstadoConversacion:
- Cargando
- Exito
- Error
- Vacio

---

# 17. Coroutines

Utilizar Kotlin Coroutines para operaciones asíncronas.

No bloquear el hilo principal.

Las peticiones HTTP deben ejecutarse de forma asíncrona.

---

# 18. Manejo de errores

Los errores provenientes de Laravel deben transformarse a estados entendibles para la interfaz.

Ejemplo:

API:
422

Aplicación:

"El mensaje no puede estar vacío."

No mostrar errores técnicos innecesarios al usuario.

---

# 19. Seguridad

Nunca incluir dentro de la aplicación:

- Credenciales de MySQL.
- Contraseñas de base de datos.
- Claves privadas de Firebase.
- Credenciales del servidor.
- Secretos del backend.

La aplicación solamente conoce la URL pública de la API.

---

# 20. URL de API

La URL base debe estar configurada mediante configuración del proyecto.

No repetir URLs en diferentes archivos.

Incorrecto:

RetrofitArchivo1 → URL
RetrofitArchivo2 → URL
Repositorio → URL

Correcto:

Una única configuración de API.

---

# 21. Código limpio

Reglas:

- Clases pequeñas.
- Métodos pequeños.
- Nombres descriptivos.
- Evitar duplicación.
- Evitar lógica dentro de la UI.
- Evitar lógica de negocio dentro del ViewModel.
- Evitar llamadas HTTP desde las pantallas.
- Evitar clases gigantes.
- Evitar funciones con demasiadas responsabilidades.
- No crear abstracciones innecesarias.

---

# 22. Principio de dependencias

Las dependencias deben apuntar hacia el dominio.

Presentación
    ↓
Dominio
    ↑
Datos

El dominio no debe conocer:

- Retrofit.
- Firebase.
- Android.
- Context.
- Views.
- Activities.
- Fragments.

---

# 23. Regla de nombres

Todo debe ser descriptivo.

Ejemplo:

Correcto:

EnviarMensajeUseCase
ObtenerMensajesUseCase
RepositorioMensajes
FuenteRemotaMensajes
ConversacionViewModel

Evitar:

Utils
Helper
Manager
Common
DataManager
ServiceManager

si no describen realmente una responsabilidad.

---

# 24. Recursos

Los textos visibles al usuario deben estar en recursos de Android.

Ejemplo:

res/values/strings.xml

No escribir textos directamente dentro de las pantallas salvo casos justificados.

---

# 25. Principio general

La aplicación debe seguir:

PANTALLA
   ↓
VIEWMODEL
   ↓
CASO DE USO
   ↓
REPOSITORIO
   ↓
FUENTE DE DATOS
   ↓
RETROFIT
   ↓
LARAVEL
   ↓
MYSQL

Para notificaciones:

LARAVEL
   ↓
FIREBASE FCM
   ↓
ANDROID
   ↓
NOTIFICACIÓN

La aplicación móvil nunca tendrá acceso directo a MySQL.

---

# 26. Objetivo arquitectónico

La arquitectura debe ser limpia, pero no innecesariamente compleja.

La prioridad es:

1. Mantenibilidad.
2. Separación de responsabilidades.
3. Seguridad.
4. Código legible.
5. Facilidad de pruebas.
6. Facilidad para agregar funcionalidades posteriormente.

No implementar patrones solamente por utilizarlos.

Cada clase, capa y abstracción debe tener una razón clara.