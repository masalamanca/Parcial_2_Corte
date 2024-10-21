package com.example.bibliotecaparcial.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bibliotecaparcial.Model.Libro
import com.example.bibliotecaparcial.Repository.LibroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LibroScreen(libroRepository: LibroRepository, onBack: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var autorId by remember { mutableStateOf("") }
    var libros by remember { mutableStateOf(listOf<Libro>()) }
    var selectedLibro by remember { mutableStateOf<Libro?>(null) }
    var idsEliminados by remember { mutableStateOf(mutableListOf<Int>()) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para limpiar los campos
    fun clearInputs() {
        titulo = ""
        genero = ""
        autorId = ""
        selectedLibro = null
    }

    // Función para cargar libros desde la base de datos
    fun loadLibros() {
        scope.launch {
            withContext(Dispatchers.IO) {
                libros = libroRepository.getAllLibros()
            }
        }
    }

    // Cargar libros al inicio
    LaunchedEffect(Unit) {
        loadLibros()
    }

    // Contenido de la pantalla
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color(0xFFE6F2FF))
    ) {
        Text(
            text = "Gestionar Libros",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF010202),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center // Esta línea es la clave para centrar el texto
        )

        // Sección de registro de libros
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text(text = "Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF1FD1D1),
                    unfocusedIndicatorColor = Color(0xFF1FD1D1)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text(text = "Género") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF1FD1D1),
                    unfocusedIndicatorColor = Color(0xFF1FD1D1)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = autorId,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        autorId = it
                    }
                },
                label = { Text(text = "Autor ID") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF1FD1D1),
                    unfocusedIndicatorColor = Color(0xFF1FD1D1)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (titulo.isNotBlank() && genero.isNotBlank() && autorId.isNotBlank()) {
                        val nuevoId = if (idsEliminados.isNotEmpty()) {
                            idsEliminados.removeAt(0)
                        } else {
                            if (libros.isEmpty()) 1 else (libros.maxOf { it.libro_id } + 1)
                        }

                        val libro = Libro(
                            libro_id = nuevoId,
                            titulo = titulo,
                            genero = genero,
                            autor_id = autorId.toInt()
                        )
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                libroRepository.insert(libro)
                            }
                            Toast.makeText(context, "Libro Registrado", Toast.LENGTH_SHORT).show()
                            loadLibros()
                            clearInputs()
                        }
                    } else {
                        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x4803A9F4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrar Libro", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar botones de Modificar y Eliminar solo si hay un libro seleccionado
            if (selectedLibro != null) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            selectedLibro?.let { libro ->
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        libroRepository.update(libro.copy(titulo = titulo, genero = genero, autor_id = autorId.toInt()))
                                    }
                                    Toast.makeText(context, "Libro Modificado", Toast.LENGTH_SHORT).show()
                                    loadLibros()
                                    clearInputs()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF476BC7)),
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text("Modificar")
                    }

                    Button(
                        onClick = {
                            selectedLibro?.let { libro ->
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        libroRepository.delete(libro)
                                        idsEliminados.add(libro.libro_id)
                                    }
                                    Toast.makeText(context, "Libro Eliminado", Toast.LENGTH_SHORT).show()
                                    loadLibros()
                                    clearInputs()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Lista de libros
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(libros) { libro ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color(0xFFF0F0F0))
                            .clickable { // Hacer clic en el libro para seleccionarlo
                                selectedLibro = libro
                                titulo = libro.titulo
                                genero = libro.genero
                                autorId = libro.autor_id.toString()
                            }
                            .padding(8.dp) // Espaciado interno
                    ) {
                        // Muestra la información en filas separadas
                        Text(text = "ID: ${libro.libro_id}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Título: ${libro.titulo}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Género: ${libro.genero}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Autor ID: ${libro.autor_id}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón para volver al menú principal
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Volver al Menú", color = Color.White)
        }
    }
}
