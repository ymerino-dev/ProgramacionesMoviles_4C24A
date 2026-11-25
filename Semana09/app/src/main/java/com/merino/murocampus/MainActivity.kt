package com.merino.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.merino.murocampus.data.PostRepositoryRemote
import com.merino.murocampus.data.remote.RetrofitClient
import com.merino.murocampus.ui.PantallaDetalle
import com.merino.murocampus.ui.PantallaLista
import com.merino.murocampus.viewmodel.PostsViewModel
import com.merino.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val repo = remember { PostRepositoryRemote(RetrofitClient.api) }
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory(repo))
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "MuroCampus",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF19679E),
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { innerPadding ->
                    if (idSeleccionado == null) {
                        PantallaLista(
                            vm = vm,
                            alSeleccionarPost = { idSeleccionado = it },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        PantallaDetalle(
                            postId = idSeleccionado!!,
                            vm = vm,
                            alVolver = { idSeleccionado = null },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}