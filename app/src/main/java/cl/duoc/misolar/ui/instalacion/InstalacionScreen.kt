package cl.duoc.misolar.ui.instalacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.CampoValidado
import java.util.Locale

@Composable
fun InstalacionScreen(
    onVolver: () -> Unit,
    viewModel: InstalacionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Al guardar vuelve al Panel
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) onVolver()
    }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Mi instalación", onVolver = onVolver) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (uiState.esEdicion) "Edita los datos de tus paneles" else "Registra tus paneles solares",
                style = MaterialTheme.typography.titleLarge
            )

            CampoValidado(
                valor = uiState.nombre,
                onCambio = { viewModel.cambiarNombre(it) },
                etiqueta = "Nombre (ej: Casa)",
                icono = Icons.Filled.Home,
                error = uiState.errorNombre
            )
            CampoValidado(
                valor = uiState.cantidadPaneles,
                onCambio = { viewModel.cambiarCantidadPaneles(it) },
                etiqueta = "Cantidad de paneles",
                icono = Icons.Filled.SolarPower,
                error = uiState.errorCantidadPaneles,
                tipoTeclado = KeyboardType.Number
            )
            CampoValidado(
                valor = uiState.potenciaPanel,
                onCambio = { viewModel.cambiarPotenciaPanel(it) },
                etiqueta = "Potencia de cada panel (W)",
                icono = Icons.Filled.Bolt,
                error = uiState.errorPotenciaPanel,
                tipoTeclado = KeyboardType.Number
            )
            CampoValidado(
                valor = uiState.fechaInstalacion,
                onCambio = { viewModel.cambiarFechaInstalacion(it) },
                etiqueta = "Fecha de instalación (dd/mm/aaaa)",
                icono = Icons.Filled.CalendarMonth,
                error = uiState.errorFechaInstalacion
            )
            CampoValidado(
                valor = uiState.precioKwh,
                onCambio = { viewModel.cambiarPrecioKwh(it) },
                etiqueta = "Precio del kWh (CLP)",
                icono = Icons.Filled.AttachMoney,
                error = uiState.errorPrecioKwh,
                tipoTeclado = KeyboardType.Number
            )

            // Potencia total en vivo
            val potenciaTotal = uiState.potenciaTotalKw
            if (potenciaTotal != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Potencia total: ${String.format(Locale.getDefault(), "%.2f", potenciaTotal)} kW",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Button(
                onClick = { viewModel.guardar() },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(if (uiState.guardando) "Guardando..." else "Guardar instalación")
            }
        }
    }
}
