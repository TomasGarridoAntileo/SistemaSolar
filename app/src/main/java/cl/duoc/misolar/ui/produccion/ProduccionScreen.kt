package cl.duoc.misolar.ui.produccion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.model.Lectura
import cl.duoc.misolar.ui.componentes.BarraInferior
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.navegacion.Rutas
import cl.duoc.misolar.util.formatearKwh
import cl.duoc.misolar.util.isoAFecha
import coil3.compose.AsyncImage

@Composable
fun ProduccionScreen(
    onNavegar: (String) -> Unit,
    onIrANuevaLectura: () -> Unit,
    viewModel: ProduccionViewModel = viewModel()
) {
    val lecturas by viewModel.lecturas.collectAsStateWithLifecycle()
    val lecturaAEliminar by viewModel.lecturaAEliminar.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { BarraSuperior(titulo = "Producción") },
        bottomBar = { BarraInferior(rutaActual = Rutas.PRODUCCION, onNavegar = onNavegar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrANuevaLectura) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva lectura")
            }
        }
    ) { innerPadding ->
        if (lecturas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes lecturas.\nToca + para agregar la primera.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Clase 05: LazyColumn solo dibuja las tarjetas visibles
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lecturas, key = { it.id }) { lectura ->
                    TarjetaLectura(
                        lectura = lectura,
                        onEliminar = { viewModel.pedirEliminar(lectura) }
                    )
                }
            }
        }
    }

    // Confirmacion antes de borrar
    val lectura = lecturaAEliminar
    if (lectura != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelarEliminar() },
            title = { Text("¿Eliminar lectura?") },
            text = { Text("Se borrará la lectura del ${isoAFecha(lectura.fecha)} (${formatearKwh(lectura.kwh)} kWh).") },
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

@Composable
private fun TarjetaLectura(lectura: Lectura, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (lectura.fotoUri != null) {
                AsyncImage(
                    model = lectura.fotoUri,
                    contentDescription = "Foto del inversor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(isoAFecha(lectura.fecha), style = MaterialTheme.typography.titleMedium)
                Text(
                    "${formatearKwh(lectura.kwh)} kWh",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (lectura.nota.isNotBlank()) {
                    Text(lectura.nota, style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar lectura")
            }
        }
    }
}
