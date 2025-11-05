package com.example.app_catalogo_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel

class CatalogoActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppcatalogofrontTheme {
                val viewModel: CatalogoViewModel = viewModel()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Catálogo") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                                }
                            }
                        )
                    }
                ) {
                    CatalogoScreen(modifier = Modifier.padding(it), viewModel = viewModel)
                }
            }
        }
    }
}
