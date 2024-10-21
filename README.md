# Proyecto: Préstamos de Libros

Este proyecto es una aplicación móvil desarrollada en Android utilizando **Kotlin** y **Jetpack Compose**. La aplicación permite gestionar los préstamos de libros en una biblioteca, incluyendo el registro, modificación, y eliminación de los préstamos. Los datos están organizados por filas, mostrando información detallada de cada préstamo.

## Funcionalidades

- **Registrar Préstamo**: Los usuarios pueden ingresar los datos necesarios para registrar un préstamo de un libro.
- **Modificar Préstamo**: Los usuarios pueden modificar los datos de un préstamo existente.
- **Eliminar Préstamo**: Los usuarios pueden eliminar un préstamo registrado.
- **Visualización de Préstamos**: Los préstamos se muestran organizados por filas, detallando el ID del libro, el ID del miembro, la fecha de préstamo y la fecha de devolución.

### Pantalla Principal: `PrestamoScreen`

Esta es la pantalla principal de la aplicación, donde se gestiona el registro, modificación y eliminación de los préstamos.

```kotlin
@Composable
fun PrestamoScreen(prestamoRepository: PrestamoRepository, onBack: () -> Unit) {
    // Variables de estado para capturar los datos ingresados
    var libroId by remember { mutableStateOf("") }
    var miembroId by remember { mutableStateOf("") }
    var fechaPrestamo by remember { mutableStateOf("") }
    var fechaDevolucion by remember { mutableStateOf("") }
    var prestamos by remember { mutableStateOf(listOf<Prestamo>()) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para limpiar los campos después de registrar/modificar un préstamo
    fun clearInputs() {
        libroId = ""
        miembroId = ""
        fechaPrestamo = ""
        fechaDevolucion = ""
    }

    // Función para cargar los préstamos desde la base de datos
    fun loadPrestamos() {
        scope.launch {
            withContext(Dispatchers.IO) {
                prestamos = prestamoRepository.getAllPrestamos() // Obtener todos los préstamos
            }
        }
    }

    // Cargar los préstamos al iniciar
    LaunchedEffect(Unit) {
        loadPrestamos()
    }

    // Diseño de la pantalla principal
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Gestionar Préstamos", style = MaterialTheme.typography.titleLarge, color = Color(0xFF6200EE))

        // Campo de texto para ingresar el Libro ID
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

        // Campo de texto para ingresar el Miembro ID
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

        // Campo para seleccionar la Fecha de Préstamo (no editable directamente)
        TextField(
            value = fechaPrestamo,
            onValueChange = { /* No se puede editar directamente */ },
            label = { Text(text = "Fecha de Préstamo") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
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

        // Campo para seleccionar la Fecha de Devolución (no editable directamente)
        TextField(
            value = fechaDevolucion,
            onValueChange = { /* No se puede editar directamente */ },
            label = { Text(text = "Fecha de Devolución") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
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

        // Botón para registrar el préstamo
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
                            prestamoRepository.insert(prestamo)
                        }
                        Toast.makeText(context, "Préstamo Registrado", Toast.LENGTH_SHORT).show()
                        loadPrestamos()
                        clearInputs()
                    }
                } else {
                    Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar Préstamo", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para modificar el préstamo
        Button(
            onClick = {
                // Lógica para modificar un préstamo
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Modificar Préstamo", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para eliminar el préstamo
        Button(
            onClick = {
                // Lógica para eliminar un préstamo
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Eliminar Préstamo", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de préstamos organizados en filas
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(prestamos) { prestamo ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Libro ID:", fontWeight = FontWeight.Bold)
                    Text(prestamo.libro_id.toString())
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Miembro ID:", fontWeight = FontWeight.Bold)
                    Text(prestamo.miembro_id.toString())
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Fecha Préstamo:", fontWeight = FontWeight.Bold)
                    Text(prestamo.fechaPrestamo)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Fecha Devolución:", fontWeight = FontWeight.Bold)
                    Text(prestamo.fechaDevolucion)
                }
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al menú principal
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Volver al Menú", color = Color.White)
        }
    }
}

