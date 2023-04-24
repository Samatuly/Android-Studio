package com.example.tengrinews.Favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoritesEntity2")
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var fId: Int? = null,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "image")
    val image: String,
//    @ColumnInfo(name = "isSave")
//    val ifSave: Boolean
    )