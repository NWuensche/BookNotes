package com.nwuensche.booknotes.presenter

import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.nwuensche.booknotes.model.AppDatabase
import com.nwuensche.booknotes.model.Book
import com.nwuensche.booknotes.view.MenuView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import com.android.volley.toolbox.StringRequest


/**
 * Created by nwuensche on 03.02.18.
 */

class MainPresenter(private val view: MenuView) : Presenter {
    private lateinit var db: AppDatabase

    override fun onCreate() {
        Observable
                .fromCallable { Room.databaseBuilder(view.context, AppDatabase::class.java,  "db").build() }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe {
                    database
                    ->
                    db = database
                    showBooks()
                }
    }

    private fun showBooks() {
        Observable
                .fromCallable { db.bookDao().getAll() }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe {
                    view.updateBookList(it)
                }
    }

    fun showBookNotes(bookTitle: String) {
        Observable
                .fromCallable { db.bookDao().getBook(bookTitle) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe {
                    book
                    ->
                    view.showBookNotes(title = book.title, notes = book.info)
                }
    }

    fun addBook(isbn: String) {
        val exampleRequestQueue = Volley.newRequestQueue(view.context)

        val jsObjRequest = StringRequest(Request.Method.GET, "https://www.amazon.com/s/field-keywords=$isbn", Response.Listener<String>
            { response
            ->
                Observable
                        .fromCallable { db.bookDao().insertAll(Book(title = response.split("<h2 data-attribute=\"")[1].split("\"")[0]))}
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe {this.showBooks()}
            },
                Response.ErrorListener {}
        )
        exampleRequestQueue.add(jsObjRequest)
    }

    fun updateBook(title: String, notes: String) {
        Observable
                .fromCallable { db.bookDao().getBook(title) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe {
                    book
                    ->
                    db.bookDao().insertAll(book.apply { info = notes })
                }
    }
}
