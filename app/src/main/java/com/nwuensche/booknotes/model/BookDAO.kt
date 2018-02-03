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

    @Insert
    fun insertAll(vararg books: Book)
}
