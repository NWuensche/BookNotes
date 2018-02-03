package com.nwuensche.booknotes.view

import android.content.Context
import com.nwuensche.booknotes.model.Book

/**
 * Created by nwuensche on 03.02.18.
 */
interface MainView {
    fun showBooks(books: List<Book>)

    val context: Context
}