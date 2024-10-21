package com.example.bibliotecaparcial.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.bibliotecaparcial.Model.Libro

@Dao
interface LibroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(libro: Libro)

    @Update
    suspend fun update(libro: Libro)

    @Delete
    suspend fun delete(libro: Libro)

    @Query("SELECT * FROM libros")
    suspend fun getAllLibros(): List<Libro>

    @Query("DELETE FROM libros")
    suspend fun deleteAll()

    @Query("DELETE FROM libros WHERE libro_id = :libroId")
    suspend fun deleteById(libroId: Int)
}
