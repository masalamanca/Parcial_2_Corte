package com.example.bibliotecaparcial.Repository

import com.example.bibliotecaparcial.DAO.MiembroDao
import com.example.bibliotecaparcial.Model.Miembro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MiembroRepository(private val miembroDao: MiembroDao) {
    suspend fun insert(miembro: Miembro) {
        withContext(Dispatchers.IO) {
            miembroDao.insert(miembro)
        }
    }

    suspend fun update(miembro: Miembro) {
        withContext(Dispatchers.IO) {
            miembroDao.update(miembro)
        }
    }

    suspend fun delete(miembro: Miembro) {
        withContext(Dispatchers.IO) {
            miembroDao.delete(miembro)
        }
    }

    suspend fun getAllMiembros(): List<Miembro> {
        return withContext(Dispatchers.IO) {
            miembroDao.getAllMiembros()
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            miembroDao.deleteAll()
        }
    }

    suspend fun deleteById(miembroId: Int) {
        withContext(Dispatchers.IO) {
            miembroDao.deleteById(miembroId)
        }
    }
}
