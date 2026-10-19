package cl.duoc.misolar.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat

// Permisos que se piden juntos: precisa (GPS) y aproximada (red)
val PERMISOS_UBICACION = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

fun tienePermisoUbicacion(context: Context): Boolean {
    return PERMISOS_UBICACION.any {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

// Pide la ubicacion una sola vez y la entrega en onResultado (null si no se puede)
@SuppressLint("MissingPermission") // el permiso se revisa en la primera linea
fun pedirUbicacion(context: Context, onResultado: (Location?) -> Unit) {
    if (!tienePermisoUbicacion(context)) {
        onResultado(null)
        return
    }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Primero el GPS, si esta apagado se usa la red
    val proveedor = when {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> {
            onResultado(null)
            return
        }
    }

    // Si ya hay una ubicacion guardada en el telefono se usa altiro
    val ultima = locationManager.getLastKnownLocation(proveedor)
    if (ultima != null) {
        onResultado(ultima)
        return
    }

    // Si no, se espera la primera actualizacion y se deja de escuchar
    val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationManager.removeUpdates(this)
            onResultado(location)
        }
    }
    locationManager.requestLocationUpdates(proveedor, 0L, 0f, listener, Looper.getMainLooper())
}
