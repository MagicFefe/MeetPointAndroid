package com.swaptech.meet.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userDB: UserDB)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserDB?>

    @Query("DELETE FROM user WHERE id = :userId")
    fun deleteUserById(userId: String)
}
