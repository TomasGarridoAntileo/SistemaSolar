package cl.duoc.misolar.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import cl.duoc.misolar.model.Sesion
import cl.duoc.misolar.model.Usuario

@Database(
    entities = [Usuario::class, Sesion::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun sesionDao(): SesionDao

    // Singleton: toda la app comparte una sola instancia de la base de datos
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
