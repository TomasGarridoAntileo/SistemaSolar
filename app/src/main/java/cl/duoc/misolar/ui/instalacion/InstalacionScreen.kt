package cl.duoc.misolar.ui.instalacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.EnConstruccion

@Composable
fun InstalacionScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Mi instalación", onVolver = onVolver) }
    ) { innerPadding ->
        EnConstruccion(
            descripcion = "Aquí irá el formulario de la instalación: paneles, potencia, precio del kWh y ubicación GPS.",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
