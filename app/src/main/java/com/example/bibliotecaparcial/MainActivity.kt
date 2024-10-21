package com.example.bibliotecaparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bibliotecaparcial.Database.AppDatabase
import com.example.bibliotecaparcial.Repository.AutorRepository
import com.example.bibliotecaparcial.Repository.LibroRepository
import com.example.bibliotecaparcial.Repository.MiembroRepository
import com.example.bibliotecaparcial.Repository.PrestamoRepository
import com.example.bibliotecaparcial.Screen.AutorScreen
import com.example.bibliotecaparcial.Screen.LibroScreen
import com.example.bibliotecaparcial.Screen.MiembroScreen
import com.example.bibliotecaparcial.Screen.PrestamoScreen
import com.example.bibliotecaparcial.ui.theme.BibliotecaParcialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BibliotecaParcialTheme {
                // Crear una instancia de la base de datos y los repositorios
                val db = AppDatabase.getDatabase(context = this)
                val libroRepository = remember { LibroRepository(db.libroDao()) }
                val autorRepository = remember { AutorRepository(db.autorDao()) }
                val miembroRepository = remember { MiembroRepository(db.miembroDao()) }
                val prestamoRepository = remember { PrestamoRepository(db.prestamoDao()) }

                // Llamar a la función de menú principal con navegación
                MainMenuScreen(
                    libroRepository = libroRepository,
                    autorRepository = autorRepository,
                    miembroRepository = miembroRepository,
                    prestamoRepository = prestamoRepository
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    libroRepository: LibroRepository,
    autorRepository: AutorRepository,
    miembroRepository: MiembroRepository,
    prestamoRepository: PrestamoRepository
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE6F2FF)) // Color de fondo agradable
            ) {
                // Tarjeta centrada
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "PRESTAMO DE LIBROS",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth() // Tarjeta ocupando todo el ancho disponible
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Color.Gray),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { navController.navigate("libros") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFF17C5BF
                                )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Registrar Libro", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { navController.navigate("autores") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFF17C5BF
                                )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Registrar Autor", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { navController.navigate("miembros") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFF17C5BF
                                )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Registrar Miembro", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { navController.navigate("prestamos") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFF17C5BF
                                )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Registrar Préstamo", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        composable("libros") {
            LibroScreen(libroRepository = libroRepository, onBack = { navController.popBackStack() })
        }

        composable("autores") {
            AutorScreen(autorRepository = autorRepository, onBack = { navController.popBackStack() })
        }

        composable("miembros") {
            MiembroScreen(miembroRepository = miembroRepository, onBack = { navController.popBackStack() })
        }

        composable("prestamos") {
            PrestamoScreen(prestamoRepository = prestamoRepository, onBack = { navController.popBackStack() })
        }
    }
}
