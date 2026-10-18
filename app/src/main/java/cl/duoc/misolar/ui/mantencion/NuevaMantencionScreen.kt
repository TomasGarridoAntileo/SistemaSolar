package cl.duoc.misolar.ui.mantencion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.EnConstruccion

@Composable
fun NuevaMantencionScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Nueva mantención", onVolver = onVolver) }
    ) { innerPadding ->
        EnConstruccion(
            descripcion = "Aquí irá el formulario de mantención: tipo, fecha, descripción y foto.",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
