package com.example.bibliotecaparcial.Database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotecaparcial.DAO.AutorDao
import com.example.bibliotecaparcial.DAO.LibroDao
import com.example.bibliotecaparcial.DAO.MiembroDao
import com.example.bibliotecaparcial.DAO.PrestamoDao
import com.example.bibliotecaparcial.Model.Libro
import com.example.bibliotecaparcial.Model.Autor
import com.example.bibliotecaparcial.Model.Miembro
import com.example.bibliotecaparcial.Model.Prestamo

@Database(entities = [Libro::class, Autor::class, Miembro::class, Prestamo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun libroDao(): LibroDao
    abstract fun autorDao(): AutorDao
    abstract fun miembroDao(): MiembroDao
    abstract fun prestamoDao(): PrestamoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
