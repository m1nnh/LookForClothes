package com.sangmin.LookForClothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText memail, mpassword;
    private CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memail = findViewById(R.id.TextInputEditText_email);
        mpassword = findViewById(R.id.TextInputEditText_password);

        findViewById(R.id.find_password).setOnClickListener(this);
        findViewById(R.id.login_signup).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        CheckBox check = (CheckBox)findViewById(R.id.check);
        Boolean chk = pref.getBoolean("check", false);
        check.setChecked(chk);
    }

    public void onStop() {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        CheckBox check = (CheckBox)findViewById(R.id.check);
        editor.putBoolean("check", check.isChecked());
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        check = findViewById(R.id.check);
        if (check.isChecked() == true && user != null) {
            Toast.makeText(this, "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,TextCameraActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_password:
                startActivity(new Intent(this,FindPasswordActivity.class));
                break;
            case R.id.login_signup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.btn_login:
                mAuth.signInWithEmailAndPassword(memail.getText().toString(), mpassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, TextCameraActivity.class));
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
                break;

        }
    }
}