package cl.duoc.misolar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.misolar.ui.navegacion.Navegacion
import cl.duoc.misolar.ui.theme.MiSolarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiSolarTheme {
                Navegacion()
            }
        }
    }
}
