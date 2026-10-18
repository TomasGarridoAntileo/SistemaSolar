package cl.duoc.misolar.ui.produccion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditNote
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
import cl.duoc.misolar.util.LARGO_MAXIMO_NOTA
import cl.duoc.misolar.util.formatearKwh

@Composable
fun NuevaLecturaScreen(
    onVolver: () -> Unit,
    viewModel: NuevaLecturaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Al guardar vuelve a la lista
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) onVolver()
    }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Nueva lectura", onVolver = onVolver) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("¿Cuánto produjeron tus paneles?", style = MaterialTheme.typography.titleLarge)

            val errorGeneral = uiState.errorGeneral
            if (errorGeneral != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(errorGeneral, modifier = Modifier.padding(16.dp))
                }
            }

            CampoValidado(
                valor = uiState.fecha,
                onCambio = { viewModel.cambiarFecha(it) },
                etiqueta = "Fecha (dd/mm/aaaa)",
                icono = Icons.Filled.CalendarMonth,
                error = uiState.errorFecha
            )
            CampoValidado(
                valor = uiState.kwh,
                onCambio = { viewModel.cambiarKwh(it) },
                etiqueta = "Producción del día (kWh)",
                icono = Icons.Filled.Bolt,
                error = uiState.errorKwh,
                tipoTeclado = KeyboardType.Decimal
            )
            val maximo = uiState.maximoKwh
            if (maximo != null) {
                Text(
                    "Tu instalación produce hasta ${formatearKwh(maximo)} kWh al día",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            CampoValidado(
                valor = uiState.nota,
                onCambio = { viewModel.cambiarNota(it) },
                etiqueta = "Nota (opcional)",
                icono = Icons.Filled.EditNote,
                error = uiState.errorNota
            )
            Text(
                "${uiState.nota.length}/$LARGO_MAXIMO_NOTA",
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = { viewModel.guardar() },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(if (uiState.guardando) "Guardando..." else "Guardar lectura")
            }
        }
    }
}
