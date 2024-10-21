package com.example.bibliotecaparcial.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miembros") // Nombre de la tabla en la base de datos
data class Miembro(
    @PrimaryKey(autoGenerate = true) val miembro_id: Int = 0, // ID del miembro, autogenerado
    val nombre: String,           // Nombre del miembro
    val apellido: String,         // Apellido del miembro
    val fecha_inscripcion: String // Fecha de inscripción
)
