package cl.duoc.misolar.ui.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.misolar.ui.componentes.BarraInferior
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.navegacion.Rutas

@Composable
fun PanelScreen(
    onNavegar: (String) -> Unit,
    onIrAInstalacion: () -> Unit
) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Mi panel") },
        bottomBar = { BarraInferior(rutaActual = Rutas.PANEL, onNavegar = onNavegar) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Resumen del mes", style = MaterialTheme.typography.titleLarge)
                    Text("Aquí se mostrarán los kWh producidos, el ahorro estimado y la luz actual.")
                }
            }
            OutlinedButton(
                onClick = onIrAInstalacion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.SolarPower, contentDescription = null)
                Text("  Mi instalación")
            }
        }
    }
}
