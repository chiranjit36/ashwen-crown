package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.RaktabeejAppRoot
import com.example.ui.viewmodel.EpicViewModel

class MainActivity : ComponentActivity() {
    private val epicViewModel: EpicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RaktabeejAppRoot(viewModel = epicViewModel)
        }
    }
}
