package com.nwuensche.booknotes.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by nwuensche on 03.02.18.
 */
@Dao
interface BookDAO {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE book.title = :arg0")
    fun getBook(title: String): Book

    @Insert
    fun insertAll(vararg books: Book)

    @Query("DELETE FROM book")
    fun nuke()
}
