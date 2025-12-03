package com.example.app_catalogo_front

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppcatalogofrontTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navItems = listOf(
        R.string.iniciar_sesion,
        R.string.ver_carrito,
        R.string.ver_catalogo,
        R.string.quienes_somos
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                navItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(stringResource(id = item)) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            when (item) {
                                R.string.iniciar_sesion -> {
                                    context.startActivity(
                                        Intent(context, LoginActivity::class.java)
                                    )
                                }
                                // --- AQUÍ AGREGAMOS LA OPCIÓN DEL CARRITO ---
                                R.string.ver_carrito -> {
                                    val intent = Intent(context, CarritoActivity::class.java)
                                    context.startActivity(intent)
                                }
                                // --------------------------------------------
                                R.string.ver_catalogo -> {
                                    val intent = Intent(context, CatalogoActivity::class.java).apply {
                                        putExtra("esAdmin", false)
                                    }
                                    context.startActivity(intent)
                                }

                                R.string.quienes_somos -> {
                                    context.startActivity(Intent(context, QuienesSomosActivity::class.java))
                                }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.main_title)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = stringResource(id = R.string.menu_description)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            PantallaPrincipal(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun PantallaPrincipal(modifier: Modifier = Modifier) {
    val cakeImages = listOf(
        "https://images.pexels.com/photos/1055272/pexels-photo-1055272.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        "https://images.pexels.com/photos/2067423/pexels-photo-2067423.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        "https://images.pexels.com/photos/140831/pexels-photo-140831.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.main_subtitle),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(cakeImages.size) { index ->
            CakeImage(imageUrl = cakeImages[index])
        }

        item {
            Footer()
        }
    }
}

@Composable
fun CakeImage(imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(id = R.string.cake_image_description),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun Footer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(id = R.string.contact), style = MaterialTheme.typography.bodyMedium)
        Text(stringResource(id = R.string.address), style = MaterialTheme.typography.bodyMedium)
    }

}

fun vibrar(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibración moderna (50ms)
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Vibración antigua
            vibrator.vibrate(50)
        }
    }
}