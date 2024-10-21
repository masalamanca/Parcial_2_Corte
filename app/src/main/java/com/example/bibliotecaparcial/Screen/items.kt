package com.example.bibliotecaparcial.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaparcial.Repository.AutorRepository
import com.example.bibliotecaparcial.Repository.LibroRepository
import com.example.bibliotecaparcial.Repository.MiembroRepository
import com.example.bibliotecaparcial.Repository.PrestamoRepository

@Composable
fun UserApp(
    libroRepository: LibroRepository,
    autorRepository: AutorRepository,
    miembroRepository: MiembroRepository,
    prestamoRepository: PrestamoRepository
) {
    var currentScreen by remember { mutableStateOf("Menu") }

    when (currentScreen) {
        "Menu" -> {
            MenuScreen { screen ->
                currentScreen = screen
            }
        }
        "Libros" -> {
            LibroScreen(libroRepository) {
                currentScreen = "Menu"
            }
        }
        "Autores" -> {
            AutorScreen(autorRepository) {
                currentScreen = "Menu"
            }
        }
        "Miembros" -> {
            MiembroScreen(miembroRepository) {
                currentScreen = "Menu"
            }
        }
        "Prestamos" -> {
            PrestamoScreen(prestamoRepository) {
                currentScreen = "Menu"
            }
        }
    }
}

@Composable
fun MenuScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFEFEFEF)), // Fondo gris claro
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sistema de Biblioteca",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63) // Color gris oscuro
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tarjeta para cada opción
        MenuButton("Gestionar Libros", Color(0xFF4CAF50)) { onNavigate("Libros") }
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton("Gestionar Autores", Color(0xFF2196F3)) { onNavigate("Autores") }
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton("Gestionar Miembros", Color(0xFFFFC107)) { onNavigate("Miembros") }
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton("Gestionar Préstamos", Color(0xFFF44336)) { onNavigate("Prestamos") }
    }
}

@Composable
fun MenuButton(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = { onClick() } // Botón clicable
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
