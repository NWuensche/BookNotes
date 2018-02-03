package com.nwuensche.booknotes.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by nwuensche on 03.02.18.
 */
@Database(entities = arrayOf(Book::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDAO
}