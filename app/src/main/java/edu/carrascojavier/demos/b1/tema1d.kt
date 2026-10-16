package edu.carrascojavier.demos.b1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil3.compose.AsyncImage

// 3.2 Intent implícito (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1d_Intents_Permisos/#32-intent-impl%c3%adcito)
@Composable
fun AccionesCompartir(titulo: String, url: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Compartir texto con cualquier app instalada
        Button(onClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Te recomiendo esta película")
                putExtra(Intent.EXTRA_TEXT, "Mira '$titulo': $url")
            }
            context.startActivity(
                Intent.createChooser(intent, "Compartir con...")
            )
        }) { Text("Compartir") }

        // Abrir URL en el navegador
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            // Verificar que hay una app que puede manejar el Intent
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }) { Text("Ver en web") }

        // Enviar email
        Button(onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte@appdummy.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre $titulo")
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }) { Text("Contactar soporte") }

        // Hacer una llamada
        Button(onClick = {
            val intent = Intent(Intent.ACTION_DIAL, "tel:+34600000000".toUri())
            context.startActivity(intent)
        }) { Text("Llamar al soporte") }
    }
}

// 3.3 Recibir un resultado de otro componente (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1d_Intents_Permisos/#33-recibir-un-resultado-de-otro-componente)
@Composable
fun SeleccionarImagen() {
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    // Registrar el launcher para obtener resultado
    val seleccionadorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri   // uri es null si el usuario canceló
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { seleccionadorImagen.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        imagenUri?.let { uri ->
            Log.d("SeleccionarImagen", "URI seleccionada: $uri")
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(   // de la librería Coil, se añade en Bloque 3
                model = uri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun PantallaConPermisoCamara() {
    val context = LocalContext.current
    var tienePermiso by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var permisoDenegadoPermanentemente by remember { mutableStateOf(false) }

    // Launcher para solicitar un único permiso
    val solicitarPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        tienePermiso = concedido
        if (!concedido) {
            // Comprobar si el usuario marcó "No volver a preguntar"
            // (solo posible desde una Activity, no directamente desde Compose)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            tienePermiso -> {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Permiso de cámara concedido ✓")
                Text("Aquí iría la vista de cámara (Bloque 4)")
            }

            permisoDenegadoPermanentemente -> {
                Icon(
                    Icons.Default.Block,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text("Permiso de cámara denegado permanentemente")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        // Abrir Ajustes del sistema para que el usuario lo conceda manualmente
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) { Text("Abrir Ajustes") }
            }

            else -> {
                Text(
                    "Esta función necesita acceso a la cámara para fotografiar portadas.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { solicitarPermiso.launch(Manifest.permission.CAMERA) }) {
                    Text("Conceder permiso de cámara")
                }
            }
        }
    }
}

@Composable
fun SolicitarPermisosMultimedia() {
    val context = LocalContext.current

    // En Android 13+, los permisos de imágenes, vídeo y audio son separados
    val permisos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    var permisosEstado by remember {
        mutableStateOf(
            permisos.associateWith { permiso ->
                ContextCompat.checkSelfPermission(context, permiso) ==
                        PackageManager.PERMISSION_GRANTED
            }
        )
    }

    // Launcher para múltiples permisos a la vez
    val solicitarPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        permisosEstado = resultados
    }

    val todosConcentidos = permisosEstado.all { it.value }

    Column(modifier = Modifier.padding(16.dp)) {
        if (todosConcentidos) {
            Text("✓ Todos los permisos concedidos")
        } else {
            Text("Se necesitan permisos para acceder a la galería")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { solicitarPermisos.launch(permisos) }) {
                Text("Solicitar permisos")
            }
        }
    }
}

// Pantalla principal personalizada para el tema 1c, que combina todos los ejemplos
// anteriores y añade una barra de navegación inferior.
@Composable
fun PantallaTema1d() {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AccionesCompartir("Mi Película", "https://www.javiercarrasco.es")
            SeleccionarImagen()
            PantallaConPermisoCamara()
            SolicitarPermisosMultimedia()
        }
    }
}