package com.nwuensche.booknotes.presenter

import android.arch.persistence.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.nwuensche.booknotes.model.AppDatabase
import com.nwuensche.booknotes.model.Book
import com.nwuensche.booknotes.view.MenuView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import com.android.volley.VolleyError
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest



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

    fun showBooks() {
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

    fun addBook(newTitle: String) {
        val exampleRequestQueue = Volley.newRequestQueue(view.context)

        val jsObjRequest = JsonObjectRequest(Request.Method.GET, "https://openlibrary.org/api/books?bibkeys=ISBN:$newTitle&jscmd=details&format=json",
                null, Response.Listener<JSONObject>
            { response
            ->
                Observable
                        .fromCallable { db.bookDao().insertAll(Book(title = ((response["ISBN:$newTitle"] as JSONObject)["details"] as JSONObject)["title"]as String))}
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe {this.showBooks()}
            },
                Response.ErrorListener {
                    // TODO Auto-generated method stub
                     })
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
