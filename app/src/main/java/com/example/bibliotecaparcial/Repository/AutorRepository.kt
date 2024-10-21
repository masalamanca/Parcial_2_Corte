package com.example.bibliotecaparcial.Repository

import com.example.bibliotecaparcial.DAO.AutorDao
import com.example.bibliotecaparcial.Model.Autor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutorRepository(private val autorDao: AutorDao) {
    suspend fun insert(autor: Autor) {
        withContext(Dispatchers.IO) {
            autorDao.insert(autor)
        }
    }

    suspend fun update(autor: Autor) {
        withContext(Dispatchers.IO) {
            autorDao.update(autor)
        }
    }

    suspend fun delete(autor: Autor) {
        withContext(Dispatchers.IO) {
            autorDao.delete(autor)
        }
    }

    suspend fun getAllAutores(): List<Autor> {
        return withContext(Dispatchers.IO) {
            autorDao.getAllAutores()
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            autorDao.deleteAll()
        }
    }

    suspend fun deleteById(autorId: Int) {
        withContext(Dispatchers.IO) {
            autorDao.deleteById(autorId)
        }
    }
}