package com.example.bibliotecaparcial.Repository


import com.example.bibliotecaparcial.DAO.LibroDao
import com.example.bibliotecaparcial.Model.Libro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LibroRepository(private val libroDao: LibroDao) {
    suspend fun insert(libro: Libro) {
        withContext(Dispatchers.IO) {
            libroDao.insert(libro)
        }
    }

    suspend fun update(libro: Libro) {
        withContext(Dispatchers.IO) {
            libroDao.update(libro)
        }
    }

    suspend fun delete(libro: Libro) {
        withContext(Dispatchers.IO) {
            libroDao.delete(libro)
        }
    }

    suspend fun getAllLibros(): List<Libro> {
        return withContext(Dispatchers.IO) {
            libroDao.getAllLibros()
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            libroDao.deleteAll()
        }
    }

    suspend fun deleteById(libroId: Int) {
        withContext(Dispatchers.IO) {
            libroDao.deleteById(libroId)
        }
    }
}

