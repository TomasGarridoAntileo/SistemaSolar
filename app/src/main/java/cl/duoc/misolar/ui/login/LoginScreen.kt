package cl.duoc.misolar.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.misolar.ui.theme.MiSolarTheme

// Etapa 1: solo navegacion. El formulario validado se agrega en la etapa de Login
@Composable
fun LoginScreen(
    onIngresar: () -> Unit,
    onIrARegistro: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
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
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = onIngresar,
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
