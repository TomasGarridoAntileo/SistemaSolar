package cl.duoc.misolar.ui.componentes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import cl.duoc.misolar.util.borrarFoto
import cl.duoc.misolar.util.crearUriFotografia
import coil3.compose.AsyncImage

// Foto con camara o galeria, se usa en lecturas y mantenciones
@Composable
fun SelectorFoto(
    fotoUri: String?,
    onFotoCamara: (Uri) -> Unit,
    onFotoGaleria: (Uri) -> Unit,
    onQuitarFoto: () -> Unit
) {
    val context = LocalContext.current

    // Clase 06: rememberSaveable para no perder la URI si se gira la pantalla
    var uriPendiente by rememberSaveable { mutableStateOf<String?>(null) }

    // Clase 06: TakePicture devuelve true si se tomo la foto
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        val uri = uriPendiente
        if (uri != null) {
            if (exito) onFotoCamara(uri.toUri()) else borrarFoto(context, uri)
        }
        uriPendiente = null
    }

    // Clase 06: Photo Picker, no necesita permiso de galeria
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onFotoGaleria(uri)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Clase 06: la foto aparece y desaparece con animacion
        AnimatedVisibility(
            visible = fotoUri != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Clase 06: Coil carga la imagen desde la URI
                AsyncImage(
                    model = fotoUri,
                    contentDescription = "Foto del inversor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(16.dp))
                )
                TextButton(onClick = onQuitarFoto) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Text("  Quitar foto")
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    val uri = crearUriFotografia(context)
                    uriPendiente = uri.toString()
                    camaraLauncher.launch(uri)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = null)
                Text("  Cámara")
            }
            OutlinedButton(
                onClick = {
                    galeriaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                Text("  Galería")
            }
        }
    }
}
