package com.example.bibliotecaparcial.Screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bibliotecaparcial.Model.Prestamo
import com.example.bibliotecaparcial.Repository.PrestamoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PrestamoScreen(prestamoRepository: PrestamoRepository, onBack: () -> Unit) {
    var libroId by remember { mutableStateOf("") }
    var miembroId by remember { mutableStateOf("") }
    var fechaPrestamo by remember { mutableStateOf("") }
    var fechaDevolucion by remember { mutableStateOf("") }
    var prestamos by remember { mutableStateOf(listOf<Prestamo>()) }
    var selectedPrestamo by remember { mutableStateOf<Prestamo?>(null) } // Para almacenar el préstamo seleccionado para modificar o eliminar

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para limpiar los campos
    fun clearInputs() {
        libroId = ""
        miembroId = ""
        fechaPrestamo = ""
        fechaDevolucion = ""
        selectedPrestamo = null // Reinicia el préstamo seleccionado
    }

    // Función para cargar préstamos desde la base de datos
    fun loadPrestamos() {
        scope.launch {
            withContext(Dispatchers.IO) {
                prestamos = prestamoRepository.getAllPrestamos() // Obtener todos los préstamos
            }
        }
    }

    // Cargar préstamos al inicio
    LaunchedEffect(Unit) {
        loadPrestamos()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Gestionar Préstamos", style = MaterialTheme.typography.titleLarge, color = Color(
            0xFF000000
        )
        )

        TextField(
            value = libroId,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    libroId = it
                }
            },
            label = { Text(text = "Libro ID") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = miembroId,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    miembroId = it
                }
            },
            label = { Text(text = "Miembro ID") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = fechaPrestamo,
            onValueChange = { /* No se puede editar directamente */ },
            label = { Text(text = "Fecha de Préstamo") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Mostrar DatePicker cuando se hace clic
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            fechaPrestamo = "$year-${month + 1}-$dayOfMonth"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            enabled = false,
            leadingIcon = {
                Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = fechaDevolucion,
            onValueChange = { /* No se puede editar directamente */ },
            label = { Text(text = "Fecha de Devolución") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Mostrar DatePicker cuando se hace clic
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            fechaDevolucion = "$year-${month + 1}-$dayOfMonth"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            enabled = false,
            leadingIcon = {
                Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
            }
        )

        Button(
            onClick = {
                if (libroId.isNotBlank() && miembroId.isNotBlank() && fechaPrestamo.isNotBlank() && fechaDevolucion.isNotBlank()) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val fechaPrestamoDate = sdf.parse(fechaPrestamo)
                    val fechaDevolucionDate = sdf.parse(fechaDevolucion)

                    val prestamo = Prestamo(
                        libro_id = libroId.toInt(),
                        miembro_id = miembroId.toInt(),
                        fechaPrestamo = fechaPrestamoDate.toString(),
                        fechaDevolucion = fechaDevolucionDate.toString()
                    )

                    scope.launch {
                        withContext(Dispatchers.IO) {
                            prestamoRepository.insert(prestamo) // Insertar préstamo en la base de datos
                        }
                        Toast.makeText(context, "Préstamo Registrado", Toast.LENGTH_SHORT).show()
                        loadPrestamos() // Recargar lista de préstamos
                        clearInputs() // Limpiar campos
                    }
                } else {
                    Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x4803A9F4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar Préstamo", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para modificar y eliminar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    selectedPrestamo?.let { prestamo ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                prestamoRepository.update(prestamo.copy(
                                    libro_id = libroId.toInt(),
                                    miembro_id = miembroId.toInt(),
                                    fechaPrestamo = fechaPrestamo,
                                    fechaDevolucion = fechaDevolucion
                                )) // Modifica el préstamo en la base de datos
                            }
                            Toast.makeText(context, "Préstamo Modificado", Toast.LENGTH_SHORT).show()
                            loadPrestamos() // Recargar lista de préstamos
                            clearInputs() // Limpiar campos
                        }
                    } ?: Toast.makeText(context, "Seleccione un préstamo para modificar", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(text = "Modificar", color = Color.White)
            }

            Button(
                onClick = {
                    selectedPrestamo?.let { prestamo ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                prestamoRepository.delete(prestamo) // Elimina el préstamo de la base de datos
                            }
                            Toast.makeText(context, "Préstamo Eliminado", Toast.LENGTH_SHORT).show()
                            loadPrestamos() // Recargar lista de préstamos
                            clearInputs() // Limpiar campos
                        }
                    } ?: Toast.makeText(context, "Seleccione un préstamo para eliminar", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(text = "Eliminar", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de préstamos
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(prestamos) { prestamo ->
                Text(
                    "Libro ID: ${prestamo.libro_id}, Miembro ID: ${prestamo.miembro_id}, " +
                            "Fecha Préstamo: ${prestamo.fechaPrestamo}, Fecha Devolución: ${prestamo.fechaDevolucion}",
                    modifier = Modifier
                        .clickable {
                            selectedPrestamo = prestamo // Selecciona el préstamo para modificar o eliminar
                            libroId = prestamo.libro_id.toString()
                            miembroId = prestamo.miembro_id.toString()
                            fechaPrestamo = prestamo.fechaPrestamo
                            fechaDevolucion = prestamo.fechaDevolucion
                        }
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al menú principal
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Volver al Menú", color = Color.White)
        }
    }
}
