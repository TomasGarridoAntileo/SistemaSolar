package cl.duoc.misolar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulPanel,
    onPrimary = Color.White,
    primaryContainer = AzulPanelContenedor,
    onPrimaryContainer = Color(0xFF001B3E),
    secondary = AmarilloSol,
    onSecondary = Color.Black,
    secondaryContainer = AmarilloSolContenedor,
    onSecondaryContainer = Color(0xFF261A00),
    tertiary = VerdeEnergia,
    onTertiary = Color.White,
    tertiaryContainer = VerdeEnergiaContenedor,
    onTertiaryContainer = Color(0xFF002204),
    background = FondoClaro,
    surface = FondoClaro
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulPanelOscuro,
    onPrimary = Color(0xFF002F65),
    primaryContainer = AzulPanelContenedorOscuro,
    onPrimaryContainer = AzulPanelContenedor,
    secondary = AmarilloSolOscuro,
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = AmarilloSolContenedorOscuro,
    onSecondaryContainer = AmarilloSolContenedor,
    tertiary = VerdeEnergiaOscuro,
    onTertiary = Color(0xFF00390A),
    tertiaryContainer = VerdeEnergiaContenedorOscuro,
    onTertiaryContainer = VerdeEnergiaContenedor,
    background = FondoOscuro,
    surface = FondoOscuro
)

// Sin color dinamico para usar siempre la paleta solar
@Composable
fun MiSolarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
