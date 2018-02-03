package com.nwuensche.booknotes.presenter

import android.arch.persistence.room.Room
import com.nwuensche.booknotes.model.AppDatabase
import com.nwuensche.booknotes.model.Book
import com.nwuensche.booknotes.view.MenuView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

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
        Observable
                .fromCallable { db.bookDao().insertAll(Book(title = newTitle)) }
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe()

        this.showBooks()
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
