package com.nwuensche.booknotes.presenter

import android.content.Context
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
import com.marcoscg.licenser.Library
import com.marcoscg.licenser.License
import com.marcoscg.licenser.LicenserDialog


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

        val jsObjRequest = object: StringRequest(Request.Method.GET, "https://www.amazon.com/s/?field-keywords=$isbn", Response.Listener<String>
            { response
            ->
                Observable
                        .fromCallable { db.bookDao().insertAll(Book(title =

                            response.split("a-size-medium")[1].split(">")[1].split("</")[0]))} //Book Title
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe {this.showBooks()}
            },
                Response.ErrorListener {}
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = mutableMapOf( //Search does not work without these headers
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; rv:78.0) Gecko/20100101 Firefox/78.0",
                    "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                    "Accept-Language" to "en-US,en;q=0.5",
                    "DNT" to "1",
                    "Connection" to "keep-alive",
                    "Cookie" to "i18n-prefs=USD; appstore-devportal-locale=en_US",
                    "Upgrade-Insecure-Requests" to "1",
                    "Sec-GPC" to "1",
                    "Cache-Control" to "max-age=0"
                )
                return headers
            }
        }
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

    fun showLicenses(c: Context) { //Does not work for some reason with view.context
        LicenserDialog(c)
            .setLibrary(
                Library(
                    "Android Support Libraries",
                    "https://developer.android.com/topic/libraries/support-library/index.html",
                    License.APACHE2
                )
            )
            .setLibrary(
                Library(
                    "Licenser",
                    "https://github.com/marcoscgdev/Licenser",
                    License.MIT
                )
            )
            .setLibrary(
                Library(
                    "Code Scanner",
                    "https://github.com/yuriy-budiyev/code-scanner",
                    License.MIT
                )
            )
            .setLibrary(
                Library(
                    "Rx Java",
                    "https://github.com/ReactiveX/RxJava",
                    License.APACHE2
                )
            )
            .setPositiveButton(
                android.R.string.ok
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }
}
