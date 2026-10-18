package cl.duoc.misolar.ui.mantencion

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
fun MantencionesScreen(
    onNavegar: (String) -> Unit,
    onIrANuevaMantencion: () -> Unit
) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Mantenciones") },
        bottomBar = { BarraInferior(rutaActual = Rutas.MANTENCIONES, onNavegar = onNavegar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrANuevaMantencion) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva mantención")
            }
        }
    ) { innerPadding ->
        EnConstruccion(
            descripcion = "Aquí irá el historial de limpiezas, revisiones y reparaciones.",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
