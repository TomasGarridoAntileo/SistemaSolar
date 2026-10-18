package cl.duoc.misolar.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.ui.componentes.CampoValidado

@Composable
fun LoginScreen(
    onIngresar: () -> Unit,
    onIrARegistro: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    // La pantalla observa el StateFlow y se recompone cuando cambia el estado
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Cuando el ViewModel confirma el login, se navega al Panel una sola vez
    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            viewModel.reiniciar()
            onIngresar()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.SolarPower,
                contentDescription = null,
                modifier = Modifier.size(96.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "MiSolar",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Controla la producción y mantención de tus paneles solares",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
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

            val errorGeneral = uiState.errorGeneral
            if (errorGeneral != null) {
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
                        text = errorGeneral,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Button(
                onClick = { viewModel.ingresar() },
                enabled = !uiState.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.cargando) "Ingresando..." else "Ingresar")
            }
            TextButton(onClick = onIrARegistro) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
