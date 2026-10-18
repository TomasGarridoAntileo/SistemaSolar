package cl.duoc.misolar.ui.produccion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.EnConstruccion

@Composable
fun NuevaLecturaScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Nueva lectura", onVolver = onVolver) }
    ) { innerPadding ->
        EnConstruccion(
            descripcion = "Aquí irá el formulario de lectura: fecha, kWh, nota y foto del inversor.",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
