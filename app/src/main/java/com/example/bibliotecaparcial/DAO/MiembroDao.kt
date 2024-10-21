package com.example.bibliotecaparcial.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.bibliotecaparcial.Model.Miembro

@Dao
interface MiembroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(miembro: Miembro)

    @Update
    suspend fun update(miembro: Miembro)

    @Delete
    suspend fun delete(miembro: Miembro)

    @Query("SELECT * FROM miembros")
    suspend fun getAllMiembros(): List<Miembro>

    @Query("DELETE FROM miembros")
    suspend fun deleteAll()

    @Query("DELETE FROM miembros WHERE miembro_id = :miembroId")
    suspend fun deleteById(miembroId: Int)
}
