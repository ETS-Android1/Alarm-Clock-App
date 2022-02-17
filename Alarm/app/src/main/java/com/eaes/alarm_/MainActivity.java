package com.eaes.alarm_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    User[] usersList;
    static int numUsers;
    UserDBHelper userDBHelper;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Button loginBtn=findViewById(R.id.btn_signIn);
        Button registerBtn=findViewById(R.id.btn_register);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameInput=findViewById(R.id.et_UserName);
                EditText passwordInput=findViewById(R.id.et_Password);
                String name=nameInput.getText().toString();
                String password=passwordInput.getText().toString();

                boolean found=false;

                for(int i=0;i<numUsers;++i)
                {
                    if(name.equals(usersList[i].userName)&&password.equals(usersList[i].password))
                    {
                        found=true;
                        staticVars.userPublicId =i;
                        break;
                    }
                }
                if(found)
                {
                    //loggedIn
                    Toast.makeText(getApplicationContext(),"Logged In Successfully",Toast.LENGTH_LONG).show();

                    nameInput.setText("");
                    passwordInput.setText("");

                    // switch to other activity
                    staticVars.logout = false;
                    StopWatchHelper.Exit = false;
                    new Navigator(MainActivity.this , "Alarm");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_LONG).show();
                }
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameInput=findViewById(R.id.et_UserName);
                EditText passwordInput=findViewById(R.id.et_Password);
                String name=nameInput.getText().toString();
                String password=passwordInput.getText().toString();

                boolean exists=false;

                for(int i=0;i<numUsers;++i)
                {
                    if(name.equals(usersList[i].userName))
                    {
                        exists=true;
                        break;
                    }
                }
                if(exists)
                {
                    Toast.makeText(getApplicationContext(),"User Already Exists !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //registered
                    usersList[numUsers]=new User(name,password,numUsers++);
                    userDBHelper.createUser(name,password,numUsers);
                    Toast.makeText(getApplicationContext(),"Registered Successfully !",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void init() {
        numUsers=0;
        usersList=new User[200];
        userDBHelper=new UserDBHelper(this);

        Cursor cursor=userDBHelper.fetchUsers();

        while(!cursor.isAfterLast() && cursor!=null)
        {
            String userName,password;
            int id;
            userName=cursor.getString(0);
            password=cursor.getString(1);
            id=Integer.parseInt(cursor.getString(2));
            usersList[numUsers++]=new User(userName,password,id);
            cursor.moveToNext();
        }
    }
}

class User{
    String userName,password;
    int id;

    public User(String userName, String password, int id) {
        this.userName = userName;
        this.password = password;
        this.id = id;

    }

}