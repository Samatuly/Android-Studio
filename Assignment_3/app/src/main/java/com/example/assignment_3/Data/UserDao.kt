package com.example.assignment_3.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("Select * From user_table")
    fun getAllUser(): Flow<List<User>>

    @Query("Update user_table set name=:name, surname=:surname, email=:email, password=:password where id like:id")
    fun updateUser(id:Int, name:String, surname:String, email:String, password:String)
}