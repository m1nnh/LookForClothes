package com.sangmin.LookForClothes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private  static  final  String TAG="FindPasswordActivity";

    private EditText editTextUserEmail;
    private Button btn_find;
    private TextView textViewMessage;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

    editTextUserEmail=(EditText)findViewById(R.id.editTextUserEmail);
    progressDialog=new ProgressDialog(this);
    firebaseAuth=firebaseAuth.getInstance();

    btn_find=findViewById(R.id.btn_find);
    btn_find.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v == btn_find) {
            progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요.");
            progressDialog.show();

            String emailAddress = editTextUserEmail.getText().toString().trim();

            firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(FindPasswordActivity.this, "Email을 보냈습니다.", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {
                        Toast.makeText(FindPasswordActivity.this, "Email 전송을 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });

        }
    }
}
