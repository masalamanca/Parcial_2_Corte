package com.example.bibliotecaparcial.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale


@Entity(tableName = "prestamos")
data class Prestamo(
    @PrimaryKey(autoGenerate = true)
    val prestamo_id: Int = 0,
    val libro_id: Int,
    val miembro_id: Int,
    val fechaPrestamo: String,
    val fechaDevolucion: String
)
fun stringToDate(dateString: String): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Ajusta el formato según tu entrada
        format.parse(dateString)
    } catch (e: Exception) {
        null // Manejo de error si el formato es incorrecto
    }
}
