package cl.duoc.misolar.ui.navegacion

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.misolar.ui.instalacion.InstalacionScreen
import cl.duoc.misolar.ui.login.LoginScreen
import cl.duoc.misolar.ui.mantencion.MantencionesScreen
import cl.duoc.misolar.ui.mantencion.NuevaMantencionScreen
import cl.duoc.misolar.ui.panel.PanelScreen
import cl.duoc.misolar.ui.perfil.PerfilScreen
import cl.duoc.misolar.ui.produccion.NuevaLecturaScreen
import cl.duoc.misolar.ui.produccion.ProduccionScreen
import cl.duoc.misolar.ui.registro.RegistroScreen

private const val DURACION_TRANSICION = 300

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    // Cambia de pestana sin apilar pantallas
    val irAPestana: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.PANEL) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val volver: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN,
        // Animacion al cambiar de pantalla
        enterTransition = {
            slideInHorizontally(tween(DURACION_TRANSICION)) { it / 4 } + fadeIn(tween(DURACION_TRANSICION))
        },
        exitTransition = {
            fadeOut(tween(DURACION_TRANSICION))
        },
        popEnterTransition = {
            fadeIn(tween(DURACION_TRANSICION))
        },
        popExitTransition = {
            slideOutHorizontally(tween(DURACION_TRANSICION)) { it / 4 } + fadeOut(tween(DURACION_TRANSICION))
        }
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                onIngresar = { irAlPanelDesdeLogin(navController) },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = volver,
                onRegistroExitoso = volver
            )
        }
        composable(Rutas.PANEL) {
            PanelScreen(
                onNavegar = irAPestana,
                onIrAInstalacion = { navController.navigate(Rutas.INSTALACION) }
            )
        }
        composable(Rutas.INSTALACION) {
            InstalacionScreen(onVolver = volver)
        }
        composable(Rutas.PRODUCCION) {
            ProduccionScreen(
                onNavegar = irAPestana,
                onIrANuevaLectura = { navController.navigate(Rutas.NUEVA_LECTURA) }
            )
        }
        composable(Rutas.NUEVA_LECTURA) {
            NuevaLecturaScreen(onVolver = volver)
        }
        composable(Rutas.MANTENCIONES) {
            MantencionesScreen(
                onNavegar = irAPestana,
                onIrANuevaMantencion = { navController.navigate(Rutas.NUEVA_MANTENCION) }
            )
        }
        composable(Rutas.NUEVA_MANTENCION) {
            NuevaMantencionScreen(onVolver = volver)
        }
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onNavegar = irAPestana,
                onCerrarSesion = { cerrarSesion(navController) }
            )
        }
    }
}

// Quita Login de la pila al ingresar
private fun irAlPanelDesdeLogin(navController: NavHostController) {
    navController.navigate(Rutas.PANEL) {
        popUpTo(Rutas.LOGIN) { inclusive = true }
    }
}

// Limpia la pila y vuelve a Login
private fun cerrarSesion(navController: NavHostController) {
    navController.navigate(Rutas.LOGIN) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}
