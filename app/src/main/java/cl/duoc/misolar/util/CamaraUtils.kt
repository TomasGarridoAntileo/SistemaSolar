package cl.duoc.misolar.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.IOException

// Clase 06: crea el archivo donde la camara guarda la foto
fun crearUriFotografia(context: Context): Uri {
    val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
    val archivo = File.createTempFile("foto_", ".jpg", carpeta)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}

// La foto de la galeria se copia a la app para no perderla al cerrar
fun copiarAFotos(context: Context, origen: Uri): Uri? {
    val destino = crearUriFotografia(context)
    return try {
        context.contentResolver.openInputStream(origen).use { entrada ->
            context.contentResolver.openOutputStream(destino).use { salida ->
                if (entrada == null || salida == null) return null
                entrada.copyTo(salida)
            }
        }
        destino
    } catch (e: IOException) {
        borrarFoto(context, destino.toString())
        null
    }
}

fun borrarFoto(context: Context, uri: String?) {
    if (uri == null) return
    try {
        context.contentResolver.delete(uri.toUri(), null, null)
    } catch (e: Exception) {
        // Si ya no existe no pasa nada
    }
}
