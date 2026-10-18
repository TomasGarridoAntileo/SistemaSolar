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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.ui.componentes.BarraSuperior
import cl.duoc.misolar.ui.componentes.CampoValidado

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistroExitoso: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    var mostrarExito by rememberSaveable { mutableStateOf(false) }

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
                valor = viewModel.nombre,
                onCambio = { viewModel.cambiarNombre(it) },
                etiqueta = "Nombre",
                icono = Icons.Filled.Person,
                error = viewModel.errorNombre
            )
            CampoValidado(
                valor = viewModel.apellido,
                onCambio = { viewModel.cambiarApellido(it) },
                etiqueta = "Apellido",
                icono = Icons.Filled.Badge,
                error = viewModel.errorApellido
            )
            CampoValidado(
                valor = viewModel.correo,
                onCambio = { viewModel.cambiarCorreo(it) },
                etiqueta = "Correo",
                icono = Icons.Filled.Email,
                error = viewModel.errorCorreo,
                tipoTeclado = KeyboardType.Email
            )
            CampoValidado(
                valor = viewModel.password,
                onCambio = { viewModel.cambiarPassword(it) },
                etiqueta = "Contraseña",
                icono = Icons.Filled.Lock,
                error = viewModel.errorPassword,
                esPassword = true,
                tipoTeclado = KeyboardType.Password
            )
            CampoValidado(
                valor = viewModel.repetirPassword,
                onCambio = { viewModel.cambiarRepetirPassword(it) },
                etiqueta = "Repetir contraseña",
                icono = Icons.Filled.Lock,
                error = viewModel.errorRepetirPassword,
                esPassword = true,
                tipoTeclado = KeyboardType.Password
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.aceptaTerminos,
                    onCheckedChange = { viewModel.cambiarTerminos(it) }
                )
                Text("Acepto los términos y condiciones")
            }
            val errorTerminos = viewModel.errorTerminos
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
                onClick = {
                    if (viewModel.registrar()) mostrarExito = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Registrarse")
            }
        }
    }

    if (mostrarExito) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Cuenta creada!") },
            text = { Text("Ya puedes ingresar con tu correo y contraseña.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarExito = false
                    onRegistroExitoso()
                }) {
                    Text("Ir a ingresar")
                }
            }
        )
    }
}
