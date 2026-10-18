package cl.duoc.misolar.ui.produccion

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duoc.misolar.ui.componentes.BarraInferior
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.EnConstruccion
import cl.duoc.misolar.ui.navegacion.Rutas

@Composable
fun ProduccionScreen(
    onNavegar: (String) -> Unit,
    onIrANuevaLectura: () -> Unit
) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Producción") },
        bottomBar = { BarraInferior(rutaActual = Rutas.PRODUCCION, onNavegar = onNavegar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrANuevaLectura) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva lectura")
            }
        }
    ) { innerPadding ->
        EnConstruccion(
            descripcion = "Aquí irá la lista de lecturas de producción diaria (kWh).",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
