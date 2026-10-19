package cl.duoc.misolar.ui.mantencion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.model.Mantencion
import cl.duoc.misolar.model.TipoMantencion
import cl.duoc.misolar.ui.componentes.BarraInferior
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.navegacion.Rutas
import cl.duoc.misolar.util.isoAFecha
import coil3.compose.AsyncImage

@Composable
fun MantencionesScreen(
    onNavegar: (String) -> Unit,
    onIrANuevaMantencion: () -> Unit,
    viewModel: MantencionesViewModel = viewModel()
) {
    val mantenciones by viewModel.mantenciones.collectAsStateWithLifecycle()
    val mantencionAEliminar by viewModel.mantencionAEliminar.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { BarraSuperior(titulo = "Mantenciones") },
        bottomBar = { BarraInferior(rutaActual = Rutas.MANTENCIONES, onNavegar = onNavegar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrANuevaMantencion) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva mantención")
            }
        }
    ) { innerPadding ->
        if (mantenciones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no registras mantenciones.\nToca + para agregar una limpieza, revisión o reparación.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mantenciones, key = { it.id }) { mantencion ->
                    TarjetaMantencion(
                        mantencion = mantencion,
                        onEliminar = { viewModel.pedirEliminar(mantencion) }
                    )
                }
            }
        }
    }

    // Confirmacion antes de borrar
    val mantencion = mantencionAEliminar
    if (mantencion != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelarEliminar() },
            title = { Text("¿Eliminar mantención?") },
            text = { Text("Se borrará la ${etiquetaTipo(mantencion.tipo).lowercase()} del ${isoAFecha(mantencion.fecha)} y su foto.") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmarEliminar() }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelarEliminar() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// En Room se guarda el nombre del enum, ej: "LIMPIEZA"
private fun etiquetaTipo(tipo: String): String {
    return TipoMantencion.entries.find { it.name == tipo }?.etiqueta ?: tipo
}

@Composable
private fun TarjetaMantencion(mantencion: Mantencion, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = mantencion.fotoUri,
            contentDescription = "Foto de la mantención",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    etiquetaTipo(mantencion.tipo),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(isoAFecha(mantencion.fecha), style = MaterialTheme.typography.titleSmall)
                Text(mantencion.descripcion, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar mantención")
            }
        }
    }
}
