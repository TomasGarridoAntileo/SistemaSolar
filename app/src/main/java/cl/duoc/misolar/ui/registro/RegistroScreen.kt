package cl.duoc.misolar.ui.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.CampoValidado

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistroExitoso: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    // La pantalla observa el StateFlow y se recompone cuando cambia el estado
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { BarraSuperior(titulo = "Crear cuenta", onVolver = onVolver) }
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
                text = "Completa tus datos",
                style = MaterialTheme.typography.titleLarge
            )

            CampoValidado(
                valor = uiState.nombre,
                onCambio = { viewModel.cambiarNombre(it) },
                etiqueta = "Nombre",
                icono = Icons.Filled.Person,
                error = uiState.errorNombre
            )
            CampoValidado(
                valor = uiState.apellido,
                onCambio = { viewModel.cambiarApellido(it) },
                etiqueta = "Apellido",
                icono = Icons.Filled.Badge,
                error = uiState.errorApellido
            )
            CampoValidado(
                valor = uiState.correo,
                onCambio = { viewModel.cambiarCorreo(it) },
                etiqueta = "Correo",
                icono = Icons.Filled.Email,
                error = uiState.errorCorreo,
                tipoTeclado = KeyboardType.Email
            )
            CampoValidado(
                valor = uiState.password,
                onCambio = { viewModel.cambiarPassword(it) },
                etiqueta = "Contraseña",
                icono = Icons.Filled.Lock,
                error = uiState.errorPassword,
                esPassword = true,
                tipoTeclado = KeyboardType.Password
            )
            CampoValidado(
                valor = uiState.repetirPassword,
                onCambio = { viewModel.cambiarRepetirPassword(it) },
                etiqueta = "Repetir contraseña",
                icono = Icons.Filled.Lock,
                error = uiState.errorRepetirPassword,
                esPassword = true,
                tipoTeclado = KeyboardType.Password
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.aceptaTerminos,
                    onCheckedChange = { viewModel.cambiarTerminos(it) }
                )
                Text("Acepto los términos y condiciones")
            }
            val errorTerminos = uiState.errorTerminos
            if (errorTerminos != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = errorTerminos,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
                onClick = { viewModel.registrar() },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(if (uiState.guardando) "Guardando..." else "Registrarse")
            }
        }
    }

    if (uiState.registroExitoso) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Cuenta creada!") },
            text = { Text("Ya puedes ingresar con tu correo y contraseña.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.reiniciar()
                    onRegistroExitoso()
                }) {
                    Text("Ir a ingresar")
                }
            }
        )
    }
}
