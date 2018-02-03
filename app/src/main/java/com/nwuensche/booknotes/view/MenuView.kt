package com.nwuensche.booknotes.view

import android.content.Context
import com.nwuensche.booknotes.model.Book

/**
 * Created by nwuensche on 03.02.18.
 */
interface MenuView {
    val context: Context

    fun updateBookList(books: List<Book>)
    fun showBookNotes(title: String, notes: String)
}