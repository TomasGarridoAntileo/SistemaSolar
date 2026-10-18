package cl.duoc.misolar.ui.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.model.Instalacion
import cl.duoc.misolar.ui.componentes.BarraInferior
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.navegacion.Rutas
import cl.duoc.misolar.util.calcularAhorro
import cl.duoc.misolar.util.formatearKwh
import cl.duoc.misolar.util.formatearPesos
import java.util.Locale

@Composable
fun PanelScreen(
    onNavegar: (String) -> Unit,
    onIrAInstalacion: () -> Unit,
    viewModel: PanelViewModel = viewModel()
) {
    val instalacion by viewModel.instalacion.collectAsStateWithLifecycle()
    val kwhDelMes by viewModel.kwhDelMes.collectAsStateWithLifecycle()

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
            val datos = instalacion
            if (datos == null) {
                SinInstalacion(onIrAInstalacion)
            } else {
                ResumenInstalacion(datos, onIrAInstalacion)
            }

            ResumenMes(kwhDelMes, datos)
        }
    }
}

@Composable
private fun SinInstalacion(onIrAInstalacion: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Aún no registras tu instalación", style = MaterialTheme.typography.titleLarge)
            Text("Ingresa tus paneles para calcular tu producción y ahorro.")
            Button(onClick = onIrAInstalacion, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.SolarPower, contentDescription = null)
                Text("  Registrar instalación")
            }
        }
    }
}

@Composable
private fun ResumenInstalacion(instalacion: Instalacion, onIrAInstalacion: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(instalacion.nombre, style = MaterialTheme.typography.titleLarge)
            Text("${instalacion.cantidadPaneles} paneles de ${instalacion.potenciaPanelW} W")
            Text(
                "Potencia total: ${String.format(Locale.getDefault(), "%.2f", instalacion.potenciaTotalKw)} kW",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Instalada el ${instalacion.fechaInstalacion} · kWh a \$${instalacion.precioKwh}")
            OutlinedButton(onClick = onIrAInstalacion, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Edit, contentDescription = null)
                Text("  Editar instalación")
            }
        }
    }
}

@Composable
private fun ResumenMes(kwhDelMes: Double, instalacion: Instalacion?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Resumen del mes", style = MaterialTheme.typography.titleLarge)
            Text(
                "${formatearKwh(kwhDelMes)} kWh producidos",
                style = MaterialTheme.typography.headlineSmall
            )
            if (instalacion != null) {
                Text(
                    "Ahorro estimado: ${formatearPesos(calcularAhorro(kwhDelMes, instalacion.precioKwh))}",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                Text("Registra tu instalación para calcular el ahorro.")
            }
        }
    }
}
