package com.nwuensche.booknotes.presenter

import android.arch.persistence.room.Room
import com.nwuensche.booknotes.model.AppDatabase
import com.nwuensche.booknotes.view.MainView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by nwuensche on 03.02.18.
 */

class MainPresenter(private val view: MainView) : Presenter {
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
        view.showBooks(db.bookDao().getAll())
    }

    fun showBookNotes(bookTitle: String) {
        Observable
                .fromCallable { db.bookDao().getBook(bookTitle) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe {
                    book
                    ->
                    view.showBookNotes(book.info)
                }
    }

    fun addBook() {
       view.showDialog("Write Book Title")
    }
}
