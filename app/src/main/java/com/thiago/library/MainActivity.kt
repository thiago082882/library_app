package com.thiago.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thiago.library.repository.Repository
import com.thiago.library.room.BookEntity
import com.thiago.library.room.BooksDB
import com.thiago.library.sreens.UpdateScreen
import com.thiago.library.ui.theme.LibraryTheme
import com.thiago.library.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mContext = LocalContext.current
                    val db = BooksDB.getInstance(mContext)
                    val repository = Repository(db)
                    val myViewModel = BookViewModel(repository = repository)

                    //Navigation
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "MainScreen") {

                        composable("MainScreen") {

                            MainScreen(viewModel = myViewModel, navController)

                        }
                        composable("UpdateScreen/{bookId}") {

                            UpdateScreen(
                                viewModel = myViewModel,
                                bookId = it.arguments?.getString("bookId")
                            )

                        }

                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: BookViewModel, navController: NavHostController) {
    var inputBook by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 22.dp, start = 6.dp, end = 6.dp)

    ) {

        Text(text = "Insira um Livro em Room DB", fontSize = 22.sp)

        OutlinedTextField(
            value = inputBook,
            onValueChange = {
                inputBook = it
            },
            label = {
                Text(text = "Nome do livro")
            },
            placeholder = { Text(text = "Digite o nome do seu livro") }
        )

        Button(onClick = {
            viewModel.addBook(BookEntity(0, inputBook))
        },
        colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
            Text(text = "Inserir livro em DB")
        }

        // The Books List

        BooksList(viewModel = viewModel, navController)

    }

}


@Composable
fun BookCard(viewModel: BookViewModel, book: BookEntity, navController: NavHostController) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "" + book.id, fontSize = 24.sp,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                color = Color.Blue
            )

            Text(
                text = book.title, fontSize = 24.sp,
                modifier = Modifier.fillMaxSize(0.7f),
                color = Color.Black
            )
            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = {
                    viewModel.deleteBook(book = book)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
                IconButton(onClick = {
                    navController.navigate("UpdateScreen/${book.id}")

                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )

                }

            }
        }
    }

}


@Composable
fun BooksList(viewModel: BookViewModel, navController: NavHostController) {

    val books by viewModel.books.collectAsState(initial = emptyList())
    Column(Modifier.padding(16.dp)) {

 Text(text = "Minha Biblioteca: ", fontSize = 24.sp, color = Color.Red)
    LazyColumn() {
        items(items = books) { item ->
            BookCard(
                viewModel = viewModel,
                book = item,
                navController

            )
        }
    }
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val mContext = LocalContext.current
    val db = BooksDB.getInstance(mContext)
    val repository = Repository(db)
    val myViewModel = BookViewModel(repository = repository)
    MainScreen(viewModel = myViewModel, rememberNavController())
}

