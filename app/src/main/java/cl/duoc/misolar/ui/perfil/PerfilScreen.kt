package cl.duoc.misolar.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.misolar.ui.componentes.BarraInferior
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.navegacion.Rutas

@Composable
fun PerfilScreen(
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Perfil") },
        bottomBar = { BarraInferior(rutaActual = Rutas.PERFIL, onNavegar = onNavegar) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Datos del usuario", style = MaterialTheme.typography.titleLarge)
            Text("Aquí se mostrarán el nombre y correo del usuario con sesión iniciada.")
            Button(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Text("  Cerrar sesión")
            }
        }
    }
}
