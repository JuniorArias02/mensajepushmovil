package com.juni.mensajepush.presentacion.conversacion

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juni.mensajepush.dominio.modelos.Mensaje
import kotlin.math.roundToInt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.DoneAll

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversacionPantalla(
    viewModel: ConversacionViewModel,
    onCerrarSesion: () -> Unit,
    onIrAVinculacion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val enviando by viewModel.enviando.collectAsState()
    val mensajeRespuesta by viewModel.mensajeRespuesta.collectAsState()
    var textoMensaje by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedMediaUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Avatar", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Chat", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = onIrAVinculacion) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            if (estado !is ConversacionEstado.SinVinculacion) {
                Column {
                    AnimatedVisibility(visible = mensajeRespuesta != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(40.dp)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (mensajeRespuesta?.esMio == true) "Tú" else "Contacto",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = mensajeRespuesta?.contenido ?: "",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { viewModel.cancelarRespuesta() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancelar")
                                }
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.Transparent
                    ) {
                        Column {
                            if (selectedMediaUri != null) {
                                Box(modifier = Modifier.padding(bottom = 8.dp)) {
                                    AsyncImage(
                                        model = selectedMediaUri,
                                        contentDescription = "Media seleccionada",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    IconButton(
                                        onClick = { selectedMediaUri = null },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(24.dp)
                                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                IconButton(onClick = {
                                    mediaPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                    )
                                }) {
                                    Icon(Icons.Default.AttachFile, contentDescription = "Adjuntar archivo")
                                }
                                Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    TextField(
                                        value = textoMensaje,
                                        onValueChange = { textoMensaje = it },
                                        placeholder = { Text("Mensaje") },
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 5,
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                FloatingActionButton(
                                    onClick = {
                                        var mediaFile: File? = null
                                        if (selectedMediaUri != null) {
                                            context.contentResolver.openInputStream(selectedMediaUri!!)?.use { input ->
                                                val file = File(context.cacheDir, "media_${System.currentTimeMillis()}.jpg")
                                                FileOutputStream(file).use { output ->
                                                    input.copyTo(output)
                                                }
                                                mediaFile = file
                                            }
                                        }
                                        viewModel.enviarMensaje(textoMensaje, mediaFile)
                                        textoMensaje = ""
                                        selectedMediaUri = null
                                    },
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    shape = CircleShape,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    if (enviando) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(Icons.Default.Send, contentDescription = "Enviar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (estado) {
                is ConversacionEstado.SinVinculacion -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Aún no tienes un chat activo.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onIrAVinculacion) {
                            Text("Vincular un contacto")
                        }
                    }
                }
                is ConversacionEstado.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ConversacionEstado.Error -> {
                    Text(
                        text = (estado as ConversacionEstado.Error).mensaje,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ConversacionEstado.Vacio -> {
                    Text("No hay mensajes", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.Center))
                }
                is ConversacionEstado.Exito -> {
                    val mensajes = (estado as ConversacionEstado.Exito).mensajes
                    val mensajesReversos = mensajes.reversed()
                    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
                    val coroutineScope = rememberCoroutineScope()
                    
                    var mensajeAEditar by remember { mutableStateOf<Mensaje?>(null) }
                    var mensajeAEliminar by remember { mutableStateOf<Mensaje?>(null) }

                    if (mensajeAEditar != null) {
                        var textoEditado by remember(mensajeAEditar) { mutableStateOf(mensajeAEditar!!.contenido) }
                        AlertDialog(
                            onDismissRequest = { mensajeAEditar = null },
                            title = { Text("Editar mensaje") },
                            text = {
                                OutlinedTextField(
                                    value = textoEditado,
                                    onValueChange = { textoEditado = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.editarMensaje(mensajeAEditar!!.id, textoEditado)
                                    mensajeAEditar = null
                                }) { Text("Guardar") }
                            },
                            dismissButton = {
                                TextButton(onClick = { mensajeAEditar = null }) { Text("Cancelar") }
                            }
                        )
                    }

                    if (mensajeAEliminar != null) {
                        AlertDialog(
                            onDismissRequest = { mensajeAEliminar = null },
                            title = { Text("Eliminar mensaje") },
                            text = { Text("¿Estás seguro de que quieres eliminar este mensaje?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.eliminarMensaje(mensajeAEliminar!!.id)
                                    mensajeAEliminar = null
                                }) { Text("Eliminar") }
                            },
                            dismissButton = {
                                TextButton(onClick = { mensajeAEliminar = null }) { Text("Cancelar") }
                            }
                        )
                    }
                    
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        reverseLayout = true
                    ) {
                        items(mensajesReversos, key = { it.id }) { mensaje ->
                            BurbujaMensajeSwipeable(
                                mensaje = mensaje,
                                onResponder = { viewModel.iniciarRespuesta(mensaje) },
                                onReplyClicked = { replyId ->
                                    val index = mensajesReversos.indexOfFirst { it.id == replyId }
                                    if (index != -1) {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(index)
                                        }
                                    }
                                },
                                onEditar = { mensajeAEditar = it },
                                onEliminar = { mensajeAEliminar = it }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BurbujaMensajeSwipeable(
    mensaje: Mensaje, 
    onResponder: () -> Unit, 
    onReplyClicked: (String) -> Unit,
    onEditar: (Mensaje) -> Unit,
    onEliminar: (Mensaje) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(mensaje) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX > 150f) {
                            onResponder()
                        }
                        offsetX = 0f
                    }
                ) { change, dragAmount ->
                    if (dragAmount > 0 || offsetX > 0) { // Solo hacia la derecha
                        offsetX += dragAmount
                        if (offsetX > 200f) offsetX = 200f // Limite
                    }
                }
            }
    ) {
        if (offsetX > 50f) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Reply,
                contentDescription = "Responder",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
        ) {
            BurbujaMensaje(mensaje, onReplyClicked, onEditar, onEliminar)
        }
    }
}

@Composable
fun BurbujaMensaje(
    mensaje: Mensaje, 
    onReplyClicked: (String) -> Unit,
    onEditar: (Mensaje) -> Unit,
    onEliminar: (Mensaje) -> Unit
) {
    val esMio = mensaje.esMio
    val backgroundColor = if (esMio) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (esMio) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    
    val shape = if (esMio) {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    }

    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (esMio) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape)
                .clip(shape)
                .widthIn(min = 80.dp, max = 280.dp)
                .pointerInput(mensaje) {
                    detectTapGestures(
                        onLongPress = {
                            if (esMio && mensaje.eliminadoEn == null) {
                                showMenu = true
                            }
                        }
                    )
                }
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                if (mensaje.respuestaAMensaje != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                            .clickable { onReplyClicked(mensaje.respuestaAMensaje.id) }
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(
                                text = if (mensaje.respuestaAMensaje.esMio) "Tú" else "Contacto",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = mensaje.respuestaAMensaje.contenido,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp,
                                color = textColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                if (mensaje.mediaUrl != null && mensaje.eliminadoEn == null) {
                    AsyncImage(
                        model = mensaje.mediaUrl,
                        contentDescription = "Imagen adjunta",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                if (mensaje.contenido.isNotBlank()) {
                    Text(
                        text = mensaje.contenido,
                        color = textColor,
                        fontSize = 16.sp,
                        fontStyle = if (mensaje.eliminadoEn != null) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal
                    )
                }
                
                Row(
                    modifier = Modifier.align(Alignment.End).padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mensaje.editadoEn != null && mensaje.eliminadoEn == null) {
                        Text(
                            text = "Editado",
                            color = textColor.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = mensaje.fechaEnvio.takeLast(8).take(5),
                        color = textColor.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                    
                    if (esMio) {
                        Spacer(modifier = Modifier.width(4.dp))
                        val icon = if (mensaje.leido || mensaje.entregado) Icons.Default.DoneAll else Icons.Default.Done
                        val tint = if (mensaje.leido) MaterialTheme.colorScheme.primary else textColor.copy(alpha = 0.7f)
                        Icon(
                            imageVector = icon,
                            contentDescription = "Estado del mensaje",
                            modifier = Modifier.size(14.dp),
                            tint = tint
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        showMenu = false
                        onEditar(mensaje)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    onClick = {
                        showMenu = false
                        onEliminar(mensaje)
                    }
                )
            }
        }
    }
}
