package com.thiago.library.repository

import com.thiago.library.room.BookEntity
import com.thiago.library.room.BooksDB

class Repository(val booksDB: BooksDB) {

    suspend fun addBookToRoom(bookEntity: BookEntity) = booksDB.bookDao().addBook(bookEntity)

    fun getAllBooks() = booksDB.bookDao().getAllBooks()

    suspend fun deleteBookFromRoom(bookEntity: BookEntity) =
        booksDB.bookDao().deleteBook(bookEntity)

    suspend fun updateBook(bookEntity: BookEntity) = booksDB.bookDao().updateBook(bookEntity)

}