package com.example.bibliotecaparcial.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autores") // Nombre de la tabla en la base de datos
data class Autor(
    @PrimaryKey(autoGenerate = true) val autor_id: Int = 0, // ID del autor, autogenerado
    val nombre: String,           // Nombre del autor
    val apellido: String         // Apellido del autor
)
