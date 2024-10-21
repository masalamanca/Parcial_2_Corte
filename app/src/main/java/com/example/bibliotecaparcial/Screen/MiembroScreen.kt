package com.example.bibliotecaparcial.Screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bibliotecaparcial.Model.Miembro
import com.example.bibliotecaparcial.Repository.MiembroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MiembroScreen(miembroRepository: MiembroRepository, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fechaInscripcion by remember { mutableStateOf("") }
    var miembros by remember { mutableStateOf(listOf<Miembro>()) }
    var selectedMiembroId by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Cargar miembros
    fun loadMiembros() {
        scope.launch {
            withContext(Dispatchers.IO) {
                miembros = miembroRepository.getAllMiembros()
            }
        }
    }

    // Limpiar inputs
    fun clearInputs() {
        nombre = ""
        apellido = ""
        fechaInscripcion = ""
        selectedMiembroId = null
    }

    // Validar y convertir fecha
    fun parseFecha(fechaStr: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaStr)
        } catch (e: Exception) {
            null
        }
    }

    // Contenido de la pantalla
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color(0xFFE6F2FF)) // Color de fondo
    ) {
        Text(
            text = "Gestionar de Miembros",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF010202),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center // Esta línea es la clave para centrar el texto
        )

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(text = "Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text(text = "Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField para fecha de inscripción
        TextField(
            value = fechaInscripcion,
            onValueChange = { /* No se puede editar directamente */ },
            label = { Text(text = "Fecha de Inscripción") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Mostrar DatePicker cuando se hace clic
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            fechaInscripcion = "$year-${month + 1}-$dayOfMonth"
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

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para registrar miembro
        Button(
            onClick = {
                val fechaParsed = parseFecha(fechaInscripcion)
                if (nombre.isNotBlank() && apellido.isNotBlank() && fechaParsed != null) {
                    val miembro = Miembro(
                        nombre = nombre,
                        apellido = apellido,
                        fecha_inscripcion = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fechaParsed)
                    )
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            miembroRepository.insert(miembro)
                        }
                        Toast.makeText(context, "Miembro Registrado", Toast.LENGTH_SHORT).show()
                        clearInputs()
                        loadMiembros()
                    }
                } else {
                    Toast.makeText(context, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x4803A9F4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar Miembro", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar botones solo si hay un miembro seleccionado
        if (selectedMiembroId != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botón para modificar
                Button(
                    onClick = {
                        if (selectedMiembroId != null) {
                            val fechaParsed = parseFecha(fechaInscripcion)
                            if (nombre.isNotBlank() && apellido.isNotBlank() && fechaParsed != null) {
                                val miembroActualizado = Miembro(
                                    miembro_id = selectedMiembroId!!,
                                    nombre = nombre,
                                    apellido = apellido,
                                    fecha_inscripcion = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fechaParsed)
                                )
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        miembroRepository.update(miembroActualizado)
                                    }
                                    Toast.makeText(context, "Miembro Actualizado", Toast.LENGTH_SHORT).show()
                                    clearInputs()
                                    loadMiembros()
                                }
                            } else {
                                Toast.makeText(context, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Seleccione un miembro para modificar", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = selectedMiembroId != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    modifier = Modifier.weight(1f) // Igualar el tamaño
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Modificar Miembro")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Modificar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón para eliminar
                Button(
                    onClick = {
                        if (selectedMiembroId != null) {
                            val miembro = miembros.find { it.miembro_id == selectedMiembroId }
                            if (miembro != null) {
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        miembroRepository.delete(miembro)
                                    }
                                    Toast.makeText(context, "Miembro Eliminado", Toast.LENGTH_SHORT).show()
                                    loadMiembros()
                                    clearInputs()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Seleccione un miembro para eliminar", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = selectedMiembroId != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    modifier = Modifier.weight(1f) // Igualar el tamaño
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Miembro")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de miembros
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Permitir que la lista ocupe el espacio restante
        ) {
            items(miembros) { miembro ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedMiembroId = miembro.miembro_id
                            nombre = miembro.nombre
                            apellido = miembro.apellido
                            fechaInscripcion = miembro.fecha_inscripcion
                        }
                ) {
                    // Tarjeta de datos del miembro
                    Text("ID: ${miembro.miembro_id}")
                    Text("Nombre: ${miembro.nombre}")
                    Text("Apellido: ${miembro.apellido}")
                    Text("Inscripción: ${miembro.fecha_inscripcion}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para regresar al menú principal
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Volver al Menú Principal")
        }
    }

    // Cargar miembros al inicio
    LaunchedEffect(Unit) {
        loadMiembros()
    }
}
