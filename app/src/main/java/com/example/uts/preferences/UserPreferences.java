package com.example.uts.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.uts.model.User;

public class UserPreferences
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NOTELP = "noTelp";
    public static final String KEY_FOTO = "foto";

    public UserPreferences(Context context) {
        this.context = context;
        /* penamaan bebas namun disini digunakan "userPreferences" */
        sharedPreferences = context.getSharedPreferences("userPreferences",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUser(int id,String name, String email, String password, String noTelp, String foto){

        /* Menyimpan data login ke sharedPreferences dengan key dan value  */
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_NOTELP,noTelp);
        editor.putString(KEY_FOTO,foto);
        /* Jangan lupa commit karena kalo hanya set editonya saja tidak commit akan sia-sia */
        editor.commit();
    }

    public void setNama (String name){

        /* Menyimpan data login ke sharedPreferences dengan key dan value  */
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME,name);
        /* Jangan lupa commit karena kalo hanya set editonya saja tidak commit akan sia-sia */
        editor.commit();
    }

    public void setTelp (String noTelp){

        /* Menyimpan data login ke sharedPreferences dengan key dan value  */
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NOTELP,noTelp);
        /* Jangan lupa commit karena kalo hanya set editonya saja tidak commit akan sia-sia */
        editor.commit();
    }

    public User getUserLogin(){
        /* Mengembalikan object User untuk menampilkan data user jika user sudah login */
        String name,username,password,noTelp,foto;
        int id;

        id = sharedPreferences.getInt(KEY_ID,0);
        name = sharedPreferences.getString(KEY_NAME,null);
        username = sharedPreferences.getString(KEY_EMAIL,null);
        password = sharedPreferences.getString(KEY_PASSWORD,null);
        noTelp = sharedPreferences.getString(KEY_NOTELP,null);
        foto = sharedPreferences.getString(KEY_FOTO,null);
        return new User(id,name,username,password, noTelp, foto);
    }

    public boolean checkLogin(){
        /* Mengembalikan nilai is_login, jika sudah login otomatis nilai true jika tidak akan return false */
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public void logout(){
        /* Melakukan clear data yang ada pada sharedPreferences  , jangan lupa di commit agar data terubah*/
        editor.clear();
        editor.commit();
    }
}
