package com.example.apigpt

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apigpt.ui.theme.ApigptTheme
import com.example.apigpt.viewmodel.ContentViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.apigpt.utils.FileUtils.scanSystemForFiles


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentViewModel = ContentViewModel()
        val rootDirectory = Environment.getExternalStorageDirectory()
        val fileHashMap = scanSystemForFiles(rootDirectory.absolutePath)
        setContent {
            ApigptTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Apigpt(contentViewModel)
                    FileListScreen(fileHashMap)
                }
            }
        }
    }
}
@Composable
fun FileListScreen(fileHashMap: Map<String, List<String>>) {
    var selectedExtension by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn {
            items(fileHashMap.keys.toList()) { extension ->
                Text(
                    text = extension,
                    modifier = Modifier
                        .clickable {
                            // Quando o usuário selecionar uma extensão, armazene-a no estado
                            selectedExtension = extension
                        }
                        .padding(16.dp)
                        .fillMaxWidth(),
                    fontSize = 18.sp
                )
            }
        }

        // Exibe extensão selecionada pelo usuário
        Text(
            text = "Extensão selecionada: $selectedExtension",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp
        )
    }
}



@Composable
fun Apigpt(contentViewModel: ContentViewModel) {
    var question by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Faça sua pergunta") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                contentViewModel.sendRequest(question, "user")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }



        if (contentViewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text(contentViewModel.answer, modifier = Modifier.padding(16.dp))
        }
    }

}

