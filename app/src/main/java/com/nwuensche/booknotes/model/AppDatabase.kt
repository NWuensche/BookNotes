package com.nwuensche.booknotes.model

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Created by nwuensche on 03.02.18.
 */
@Database(entities = arrayOf(Book::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDAO
}