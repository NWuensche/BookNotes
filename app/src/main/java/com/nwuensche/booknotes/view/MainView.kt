package com.nwuensche.booknotes.view

import android.content.Context
import com.nwuensche.booknotes.model.Book

/**
 * Created by nwuensche on 03.02.18.
 */
interface MainView {
    val context: Context

    fun showBooks(books: List<Book>)
    fun showDialog(title: String)
    fun showBookNotes(notes: String)
}