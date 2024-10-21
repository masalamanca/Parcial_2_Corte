package com.example.bibliotecaparcial.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.bibliotecaparcial.Model.Prestamo

@Dao
interface PrestamoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prestamo: Prestamo)

    @Update
    suspend fun update(prestamo: Prestamo)

    @Delete
    suspend fun delete(prestamo: Prestamo)

    @Query("SELECT * FROM prestamos")
    suspend fun getAllPrestamos(): List<Prestamo>

    @Query("DELETE FROM prestamos")
    suspend fun deleteAll()

    @Query("DELETE FROM prestamos WHERE prestamo_id = :prestamoId")
    suspend fun deleteById(prestamoId: Int)
}
