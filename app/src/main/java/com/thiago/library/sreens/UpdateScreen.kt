package com.thiago.library.sreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thiago.library.room.BookEntity
import com.thiago.library.viewmodel.BookViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(viewModel: BookViewModel, bookId: String?) {

    var inputBook by remember {
        mutableStateOf("")
    }
    Column(Modifier.padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Atualizar livro existente", fontSize = 24.sp)

        OutlinedTextField(
            modifier = Modifier.padding(top = 6.dp),
            value = inputBook,
            onValueChange = {
                inputBook = it
            },
            label = {
                Text(text = "Atualização do nome do livro")
            },
            placeholder = {
                Text(text = "Nome novo do livro")
            }

        )

        Button(onClick = {

            var newBook = BookEntity(bookId!!.toInt(),inputBook)
            viewModel.updateBook(newBook)
        }
        , modifier = Modifier.padding(top=16.dp),
        colors = ButtonDefaults.buttonColors(Color.Red)) {
       Text(text = "Livro Atualizado")
        }

    }

}