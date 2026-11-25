package com.merino.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import com.merino.murocampus.data.PostRepository

class PostsViewModel(private val repo: PostRepository) : ViewModel()