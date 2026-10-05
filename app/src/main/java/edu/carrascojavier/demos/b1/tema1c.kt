package edu.carrascojavier.demos.b1

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


// 2.1 weight — distribución proporcional (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#21-weight--distribuci%c3%b3n-proporcional)
@Composable
fun DistribucionProporcional() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        // Ocupa 2/3 del espacio disponible
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) { Text("Título", color = Color.White) }

        // Ocupa 1/3 del espacio disponible
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) { Text("★ 8.5", color = Color.White) }
    }
}

// 2.2 Modificadores de interacción (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#22-modificadores-de-interacci%c3%b3n)
@Composable
fun EjemploInteraccion() {
    var seleccionado by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (seleccionado) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            )
            // clickable con ripple effect (efecto visual al pulsar)
            .clickable { seleccionado = !seleccionado }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(if (seleccionado) "✓ Favorito" else "Añadir")
    }
}

// 3.3 Tipografía (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#33-tipograf%c3%ada)
@Composable
fun EjemploTipografia() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Display Large", style = MaterialTheme.typography.displayLarge)
        Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
        Text("Title Large", style = MaterialTheme.typography.titleLarge)
        Text("Body Large", style = MaterialTheme.typography.bodyLarge)
        Text("Label Large", style = MaterialTheme.typography.labelLarge)
    }
}

// 4.1 Card (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#41-card)
@Composable
fun TarjetaPeliculaCompleta(
    titulo: String,
    puntuacion: Double,
    genero: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                AssistChip(
                    onClick = { },
                    label = { Text("★ $puntuacion") }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                genero,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// 4.3 Chips — etiquetas interactivas (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#43-chips--etiquetas-interactivas)
@Composable
fun FiltrosPorGenero() {
    val generos = listOf("Todos", "Acción", "Drama", "Comedia", "Terror", "Sci-Fi")
    var generoSeleccionado by remember { mutableStateOf("Todos") }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(generos) { genero ->
            FilterChip(
                selected = genero == generoSeleccionado,
                onClick = { generoSeleccionado = genero },
                label = { Text(genero) },
                leadingIcon = if (genero == generoSeleccionado) {
                    {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null
            )
        }
    }
}

// 4.4 ProgressIndicator — indicadores de carga (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#44-progressindicator--indicadores-de-carga)
@Composable
fun PantallaConCarga(cargando: Boolean) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (cargando) {
            // Circular — para esperas de duración desconocida
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            // Linear — para esperas con progreso conocido
            var progreso by remember { mutableFloatStateOf(0f) }

            Column {
                Button(onClick = { progreso = progreso + 0.5f }) { Text("+") }
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )
            }
        }
    }
}

// 5.1 AnimatedVisibility (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#51-animatedvisibility)
@Composable
fun EjemploAnimatedVisibility() {
    var visible by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar" else "Mostrar")
        }
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Contenido animado",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// 5.2 animate*AsState — animación de valores (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#52-animateasstate--animaci%c3%b3n-de-valores)
@Composable
fun EjemploAnimacionValor() {
    var expandido by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // El tamaño se anima automáticamente al cambiar 'expandido'
    val tamanyoAnimado by animateDpAsState(
        targetValue = if (expandido) 200.dp else 80.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tamaño"
    )
    val colorAnimado by animateColorAsState(
        targetValue = if (expandido) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "color"
    )

    Box(
        modifier = Modifier
            .size(tamanyoAnimado)
            .background(colorAnimado, RoundedCornerShape(12.dp))
            .clickable { expandido = !expandido },
        contentAlignment = Alignment.Center
    ) {
        Text(if (expandido) "✕ Cerrar" else "▶ Abrir")
    }
}

// 6. Canvas — gráficos personalizados (https://documentation.javiercarrasco.es/pmdm/B1/B1-T1c_UI_Avanzada/#6-canvas--gr%c3%a1ficos-personalizados)
@Composable
fun IndicadorPuntuacion(puntuacion: Float, modifier: Modifier = Modifier) {
    // puntuacion: valor de 0.0 a 10.0
    val progreso = (puntuacion / 10f).coerceIn(0f, 1f)
    val colorArco = when {
        progreso >= 0.8f -> Color(0xFF4CAF50)   // verde — muy buena
        progreso >= 0.6f -> Color(0xFFFF9800)   // naranja — buena
        else -> Color(0xFFF44336)   // rojo — mala
    }

    Canvas(modifier = modifier.size(80.dp)) {
        val tamanyoArco = size.minDimension
        val grosor = tamanyoArco * 0.1f

        // Arco de fondo (gris)
        drawArc(
            color = Color.LightGray,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = grosor, cap = StrokeCap.Round)
        )
        // Arco de progreso
        drawArc(
            color = colorArco,
            startAngle = 135f,
            sweepAngle = 270f * progreso,
            useCenter = false,
            style = Stroke(width = grosor, cap = StrokeCap.Round)
        )
    }
}


// Definición de las secciones de la app
data class SeccionNavegacion(
    val ruta: String,
    val icono: ImageVector,
    val etiqueta: String
)

val secciones = listOf(
    SeccionNavegacion("inicio", Icons.Default.Home, "Inicio"),
    SeccionNavegacion("buscar", Icons.Default.Search, "Buscar"),
    SeccionNavegacion("favoritos", Icons.Default.Favorite, "Favoritos"),
    SeccionNavegacion("perfil", Icons.Default.Person, "Perfil")
)


@Composable
fun PantallaTema1c() {
    var seccionActual by remember { mutableStateOf("inicio") }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                secciones.forEach { seccion ->
                    NavigationBarItem(
                        selected = seccionActual == seccion.ruta,
                        onClick = { seccionActual = seccion.ruta },
                        icon = {
                            Icon(seccion.icono, contentDescription = seccion.etiqueta)
                        },
                        label = { Text(seccion.etiqueta) }
                    )
                }
            }
        }) { innerPadding ->
        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DistribucionProporcional()
            EjemploInteraccion()
            EjemploTipografia()
            TarjetaPeliculaCompleta(
                titulo = "El Padrino",
                puntuacion = 9.2,
                genero = "Crimen, Drama",
                onClick = {
                    Toast.makeText(
                        context,
                        "El Padrino pulsada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            IndicadorPuntuacion(6.5f)

            Box(modifier = Modifier.padding(innerPadding)) {
                when (seccionActual) {
                    "inicio" -> Text("Pantalla de inicio")
                    "buscar" -> Text("Pantalla de búsqueda")
                    "favoritos" -> Text("Pantalla de favoritos")
                    "perfil" -> Text("Pantalla de perfil")
                }
            }
            EjemploAnimatedVisibility()
            EjemploAnimacionValor()
            FiltrosPorGenero()
            PantallaConCarga(cargando = true)
        }
    }
}