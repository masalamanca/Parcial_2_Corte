package com.example.bibliotecaparcial.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libros") // Nombre de la tabla en la base de datos
data class Libro(
    @PrimaryKey(autoGenerate = true) val libro_id: Int = 0, // ID del libro, autogenerado
    val titulo: String,          // Título del libro
    val genero: String,          // Género del libro
    val autor_id: Int           // ID del autor
)
