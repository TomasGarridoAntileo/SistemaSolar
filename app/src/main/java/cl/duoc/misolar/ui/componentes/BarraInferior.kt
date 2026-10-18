package cl.duoc.misolar.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cl.duoc.misolar.ui.navegacion.Rutas

private data class Pestana(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

private val pestanas = listOf(
    Pestana(Rutas.PANEL, "Panel", Icons.Filled.WbSunny),
    Pestana(Rutas.PRODUCCION, "Producción", Icons.Filled.Bolt),
    Pestana(Rutas.MANTENCIONES, "Mantención", Icons.Filled.Build),
    Pestana(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

@Composable
fun BarraInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        pestanas.forEach { pestana ->
            NavigationBarItem(
                selected = rutaActual == pestana.ruta,
                onClick = {
                    if (rutaActual != pestana.ruta) onNavegar(pestana.ruta)
                },
                icon = { Icon(pestana.icono, contentDescription = pestana.titulo) },
                label = { Text(pestana.titulo) }
            )
        }
    }
}
