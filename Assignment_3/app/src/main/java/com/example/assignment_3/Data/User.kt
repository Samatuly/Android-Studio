package com.example.assignment_3.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "surname")
    var surname: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "password")
    var password: String
    )
