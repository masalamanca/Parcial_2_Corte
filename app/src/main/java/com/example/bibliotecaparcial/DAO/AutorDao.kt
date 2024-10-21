package com.example.bibliotecaparcial.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.bibliotecaparcial.Model.Autor

@Dao
interface AutorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(autor: Autor)

    @Update
    suspend fun update(autor: Autor)

    @Delete
    suspend fun delete(autor: Autor)

    @Query("SELECT * FROM autores")
    suspend fun getAllAutores(): List<Autor>

    @Query("DELETE FROM autores")
    suspend fun deleteAll()

    @Query("DELETE FROM autores WHERE autor_id = :autorId")
    suspend fun deleteById(autorId: Int)

}
