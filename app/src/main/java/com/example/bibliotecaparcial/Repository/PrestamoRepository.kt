package com.example.bibliotecaparcial.Repository

import com.example.bibliotecaparcial.DAO.PrestamoDao
import com.example.bibliotecaparcial.Model.Prestamo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrestamoRepository(private val prestamoDao: PrestamoDao) {
    suspend fun insert(prestamo: Prestamo) {
        withContext(Dispatchers.IO) {
            prestamoDao.insert(prestamo)
        }
    }

    suspend fun update(prestamo: Prestamo) {
        withContext(Dispatchers.IO) {
            prestamoDao.update(prestamo)
        }
    }

    suspend fun delete(prestamo: Prestamo) {
        withContext(Dispatchers.IO) {
            prestamoDao.delete(prestamo)
        }
    }

    suspend fun getAllPrestamos(): List<Prestamo> {
        return withContext(Dispatchers.IO) {
            prestamoDao.getAllPrestamos()
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            prestamoDao.deleteAll()
        }
    }

    suspend fun deleteById(prestamoId: Int) {
        withContext(Dispatchers.IO) {
            prestamoDao.deleteById(prestamoId)
        }
    }
}
