package com.nwuensche.booknotes.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by nwuensche on 03.02.18.
 */
@Entity(tableName = "book")
data class Book(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var title: String = "",
        var info: String = ""
)