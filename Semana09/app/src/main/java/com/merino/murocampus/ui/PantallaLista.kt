package com.merino.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merino.murocampus.viewmodel.PostsViewModel
import com.merino.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    alSeleccionarPost: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val s = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorConReintento(
            mensaje = s.mensaje,
            alReintentar = { vm.cargar() }
        )
        is UiState.Exito -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(s.datos, key = { it.id }) { post ->
                    TarjetaPost(
                        post = post,
                        onClick = { alSeleccionarPost(post.id) }
                    )
                }
            }
        }
    }
}