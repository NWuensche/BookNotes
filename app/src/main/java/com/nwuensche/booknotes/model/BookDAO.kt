package com.nwuensche.booknotes.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by nwuensche on 03.02.18.
 */
@Dao
interface BookDAO {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE book.title = :title")
    fun getBook(title: String): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg books: Book)

    @Query("DELETE FROM book")
    fun nuke()
}
