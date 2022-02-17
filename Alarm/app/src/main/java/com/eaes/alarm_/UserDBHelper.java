package com.eaes.alarm_;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {
    private static String databaseName = "userDatabase";
    SQLiteDatabase userDatabase;
    public UserDBHelper(Context context) {
        super(context,databaseName , null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Users (Username String primary key," +
                "UserId integer ," +
                "Password String )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Users");
        onCreate(db);
    }

    public void createUser(String username , String password ,  int userId){
        ContentValues row = new ContentValues();
        row.put("Username" , username);
        row.put("UserId",userId);
        row.put("Password",password);

        userDatabase = getWritableDatabase();
        userDatabase.insert("Users" , null , row);
        userDatabase.close();
        //userDatabase.execSQL("DELETE FROM Users WHERE Username = ?",new String[]{"x"});

    }

    public Cursor fetchUsers(){
        userDatabase = getReadableDatabase();
        String[] rowDetails = {"Username","Password","UserId"};
        Cursor cursor = userDatabase.query("Users", rowDetails , null,null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();
        userDatabase.close();
        return cursor;
    }
}

