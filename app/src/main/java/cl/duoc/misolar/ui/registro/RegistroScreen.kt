package cl.duoc.misolar.ui.registro

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.EnConstruccion

@Composable
fun RegistroScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Crear cuenta", onVolver = onVolver) }
    ) { innerPadding ->
        EnConstruccion(
            descripcion = "Aquí irá el formulario de registro con validaciones.",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
