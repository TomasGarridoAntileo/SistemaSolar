package cl.duoc.misolar.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import cl.duoc.misolar.model.Instalacion
import cl.duoc.misolar.model.Lectura
import cl.duoc.misolar.model.Mantencion
import cl.duoc.misolar.model.Sesion
import cl.duoc.misolar.model.Usuario

@Database(
    entities = [
        Usuario::class,
        Sesion::class,
        Instalacion::class,
        Lectura::class,
        Mantencion::class
    ],
    // Version 2: se agregaron instalaciones, lecturas y mantenciones
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun sesionDao(): SesionDao
    abstract fun instalacionDao(): InstalacionDao
    abstract fun lecturaDao(): LecturaDao

    // Clase 05: singleton, una sola base de datos para toda la app
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "misolar.db"
                )
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
