package cl.duoc.misolar.ui.mantencion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.model.TipoMantencion
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.CampoValidado
import cl.duoc.misolar.ui.componentes.SelectorFoto
import cl.duoc.misolar.util.LARGO_MAXIMO_DESCRIPCION

@Composable
fun NuevaMantencionScreen(
    onVolver: () -> Unit,
    viewModel: NuevaMantencionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Al guardar vuelve a la lista
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) onVolver()
    }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Nueva mantención", onVolver = onVolver) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("¿Qué le hiciste a tus paneles?", style = MaterialTheme.typography.titleLarge)

            // Un chip por cada tipo del enum
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoMantencion.entries.forEach { tipo ->
                    FilterChip(
                        selected = uiState.tipo == tipo,
                        onClick = { viewModel.cambiarTipo(tipo) },
                        label = { Text(tipo.etiqueta) },
                        leadingIcon = if (uiState.tipo == tipo) {
                            { Icon(Icons.Filled.Check, contentDescription = null) }
                        } else {
                            null
                        }
                    )
                }
            }
            MensajeError(uiState.errorTipo)

            CampoValidado(
                valor = uiState.fecha,
                onCambio = { viewModel.cambiarFecha(it) },
                etiqueta = "Fecha (dd/mm/aaaa)",
                icono = Icons.Filled.CalendarMonth,
                error = uiState.errorFecha
            )
            CampoValidado(
                valor = uiState.descripcion,
                onCambio = { viewModel.cambiarDescripcion(it) },
                etiqueta = "Descripción (ej: limpieza con agua y paño)",
                icono = Icons.Filled.Description,
                error = uiState.errorDescripcion,
                lineasMaximas = 4
            )
            Text(
                "${uiState.descripcion.length}/$LARGO_MAXIMO_DESCRIPCION",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                "Foto de evidencia",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            SelectorFoto(
                fotoUri = uiState.fotoUri,
                onFotoCamara = { viewModel.ponerFotoCamara(it) },
                onFotoGaleria = { viewModel.ponerFotoGaleria(it) },
                onQuitarFoto = { viewModel.quitarFoto() }
            )
            MensajeError(uiState.errorFoto)

            Button(
                onClick = { viewModel.guardar() },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(if (uiState.guardando) "Guardando..." else "Guardar mantención")
            }
        }
    }
}

// Error para lo que no es un campo de texto (tipo y foto)
@Composable
private fun MensajeError(error: String?) {
    if (error != null) {
        Text(
            error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
