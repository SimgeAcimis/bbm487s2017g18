package com.example.enes_karabulut.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void register(View view){

        Toast.makeText(SignUpActivity.this, "You have successfully registered. Please Log In",
                Toast.LENGTH_LONG).show();
    }
}
