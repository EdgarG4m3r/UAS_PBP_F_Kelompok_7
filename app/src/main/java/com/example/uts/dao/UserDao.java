/*package com.example.uts.dao;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;

import com.example.uts.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE username=:username AND password=:password")
    User attemptLogin(String username, String password);

    @Insert
    void registerUser(User user);
}
*/