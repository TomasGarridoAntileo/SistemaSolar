package cl.duoc.misolar.ui.instalacion

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import cl.duoc.misolar.util.PERMISOS_UBICACION
import cl.duoc.misolar.util.formatearCoordenadas
import java.util.Locale

@Composable
fun InstalacionScreen(
    onVolver: () -> Unit,
    viewModel: InstalacionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // El permiso se pide recien cuando el usuario toca el boton
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            viewModel.buscarUbicacion()
        } else {
            viewModel.permisoUbicacionRechazado()
        }
    }

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

            TarjetaUbicacion(
                latitud = uiState.latitud,
                longitud = uiState.longitud,
                buscando = uiState.buscandoUbicacion,
                mensaje = uiState.mensajeUbicacion,
                onBuscar = { permisoLauncher.launch(PERMISOS_UBICACION) }
            )

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

@Composable
private fun TarjetaUbicacion(
    latitud: Double?,
    longitud: Double?,
    buscando: Boolean,
    mensaje: String?,
    onBuscar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Ubicación (opcional)", style = MaterialTheme.typography.titleMedium)
            if (latitud != null && longitud != null) {
                Text("📍 ${formatearCoordenadas(latitud, longitud)}")
            } else {
                Text("Guarda dónde están tus paneles con el GPS del teléfono.")
            }
            if (mensaje != null) {
                Text(
                    mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedButton(
                onClick = onBuscar,
                enabled = !buscando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (buscando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Text("  Buscando ubicación...")
                } else {
                    Icon(
                        imageVector = if (latitud != null) Icons.Filled.LocationOn else Icons.Filled.MyLocation,
                        contentDescription = null
                    )
                    Text(if (latitud != null) "  Actualizar ubicación" else "  Usar mi ubicación actual")
                }
            }
        }
    }
}
