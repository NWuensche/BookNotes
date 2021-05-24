package com.nwuensche.booknotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Created by nwuensche on 03.02.18.
 */
@Entity(tableName = "book")
data class Book(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,// TODO Title als Primary Key
        var title: String = "",
        var info: String = ""
)