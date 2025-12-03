package com.example.app_catalogo_front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel

class CatalogoActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // VIENE DESDE EL LOGIN: TRUE = ADMIN, FALSE = CLIENTE
        val esAdmin = intent.getBooleanExtra("esAdmin", false)
        val actividad = this@CatalogoActivity

        setContent {
            AppcatalogofrontTheme {
                val viewModel: CatalogoViewModel = viewModel()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Catálogo") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        actividad.startActivity(
                                            Intent(
                                                actividad,
                                                CarritoActivity::class.java
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingCart,
                                        contentDescription = "Ver carrito",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFFFFF5E5), // mismo estilo suave
                                titleContentColor = Color(0xFF5D4037),
                                navigationIconContentColor = Color(0xFF5D4037),
                                actionIconContentColor = Color(0xFF5D4037)
                            )
                        )
                    }
                ) { paddingValues ->
                    CatalogoScreen(
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel,
                        esAdmin = esAdmin
                    )
                }
            }
        }
    }
}
