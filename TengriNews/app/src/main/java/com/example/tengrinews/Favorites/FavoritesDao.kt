package com.example.tengrinews.Favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritesDao {

    @Insert
    fun insertNews(FavoritesEntity: FavoritesEntity)

    @Query("select * from FavoritesEntity2")
    fun getAllSavedNews(): Flow<List<FavoritesEntity>>


    @Query("delete from FavoritesEntity2 where image like :img")
    fun deleteNewsByUrl(img: String)

}