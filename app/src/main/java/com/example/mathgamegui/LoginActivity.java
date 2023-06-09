package com.example.mathgamegui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    TextInputEditText etLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btn_login);
        etLogin = findViewById(R.id.et_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etLogin.getText().toString().length() == 0) {
                    Toast.makeText(view.getContext(), "Введите ваше имя", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, QuizActivity.class);
                    intent.putExtra("key", etLogin.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

}