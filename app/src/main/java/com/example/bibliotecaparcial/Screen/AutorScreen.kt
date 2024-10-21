package com.example.bibliotecaparcial.Screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bibliotecaparcial.Model.Autor
import com.example.bibliotecaparcial.Repository.AutorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AutorScreen(autorRepository: AutorRepository, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var autorIdToUpdate by remember { mutableStateOf(-1) } // Para actualizar autores
    var autores by remember { mutableStateOf(listOf<Autor>()) }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) } // Autor seleccionado

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para limpiar los campos
    fun clearInputs() {
        nombre = ""
        apellido = ""
        autorIdToUpdate = -1
        selectedAutor = null
    }

    // Función para cargar autores desde la base de datos
    fun loadAutores() {
        scope.launch {
            withContext(Dispatchers.IO) {
                autores = autorRepository.getAllAutores() // Obtener todos los autores
            }
        }
    }

    // Cargar autores al inicio
    LaunchedEffect(Unit) {
        loadAutores()
    }

    // Usar un Box para acomodar el contenido y el botón en la parte inferior
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Registro Autores", style = MaterialTheme.typography.titleLarge, color = Color(0xFF000000))

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(text = "Nombre") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF1FD1D1), // Color de la línea inferior cuando está enfocado
                    unfocusedIndicatorColor = Color(0xFF1FD1D1), // Color de la línea inferior cuando no está enfocado
                    focusedContainerColor = Color.White, // Color del fondo del campo
                    unfocusedContainerColor = Color.White // Color del fondo del campo cuando no está enfocado
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text(text = "Apellido") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF1FD1D1), // Color de la línea inferior cuando está enfocado
                    unfocusedIndicatorColor = Color(0xFF1FD1D1), // Color de la línea inferior cuando no está enfocado
                    focusedContainerColor = Color.White, // Color del fondo del campo
                    unfocusedContainerColor = Color.White // Color del fondo del campo cuando no está enfocado
                )
            )

            Button(
                onClick = {
                    if (nombre.isNotBlank() && apellido.isNotBlank()) {
                        val autor = Autor(
                            nombre = nombre,
                            apellido = apellido
                        )
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                autorRepository.insert(autor) // Insertar autor en la base de datos
                            }
                            Toast.makeText(context, "Autor Registrado", Toast.LENGTH_SHORT).show()
                            loadAutores() // Recargar lista de autores
                            clearInputs() // Limpiar campos
                        }
                    } else {
                        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x4803A9F4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrar Autor", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de Modificar y Eliminar solo si hay un autor seleccionado
            selectedAutor?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            // Actualizar autor
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    autorRepository.update(
                                        it.copy(nombre = nombre, apellido = apellido)
                                    ) // Actualizar autor en la base de datos
                                }
                                Toast.makeText(context, "Autor Modificado", Toast.LENGTH_SHORT).show()
                                loadAutores() // Recargar lista de autores
                                clearInputs() // Limpiar campos
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF476BC7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Modificar", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre botones

                    Button(
                        onClick = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    autorRepository.delete(it) // Eliminar autor
                                }
                                Toast.makeText(context, "Autor Eliminado", Toast.LENGTH_SHORT).show()
                                loadAutores() // Recargar lista de autores
                                clearInputs() // Limpiar campos
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Eliminar", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espaciado después de los botones
            }

            // Lista de autores
            LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
                items(autores) { autor ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedAutor = autor // Establecer autor seleccionado
                                nombre = autor.nombre
                                apellido = autor.apellido
                                autorIdToUpdate = autor.autor_id // Establecer ID del autor a actualizar
                            }
                    ) {
                        Text(text = "ID: ${autor.autor_id}")
                        Text(text = "Nombre: ${autor.nombre}")
                        Text(text = "Apellido: ${autor.apellido}")
                    }
                }
            }
        }

        // Botón de Volver al Menú Principal en la parte inferior
        Button(
            onClick = { onBack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF263588)),
            modifier = Modifier
                .align(Alignment.BottomCenter) // Alinear al centro en la parte inferior
                .padding(16.dp) // Agregar un poco de padding
                .fillMaxWidth() // Hacer que el botón ocupe el ancho completo
        ) {
            Text(text = "Volver al Menú Principal", color = Color.White)
        }
    }
}


