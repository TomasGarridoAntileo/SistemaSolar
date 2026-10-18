package cl.duoc.misolar.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.misolar.ui.componentes.CampoValidado
import cl.duoc.misolar.ui.theme.MiSolarTheme

@Composable
fun LoginScreen(
    onIngresar: () -> Unit,
    onIrARegistro: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    // La pantalla observa el StateFlow y se recompone cuando cambia el estado
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

            Button(
                onClick = {
                    if (viewModel.ingresar()) onIngresar()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }
            TextButton(onClick = onIrARegistro) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MiSolarTheme {
        LoginScreen(onIngresar = {}, onIrARegistro = {})
    }
}
