package com.sangmin.LookForClothes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText e1;
    FirebaseAuth auth;
    ProgressDialog dialog;
    private Button btn_complete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dialog=new ProgressDialog(this);
        e1=(EditText)findViewById((R.id.changepassword));
        auth=FirebaseAuth.getInstance();
        btn_complete=findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            dialog.setMessage("Changing password, Please wait!!");
            dialog.show();

            user.updatePassword(e1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Your Password has been change.", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        finish();
                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                    }
                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Password could not be change.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
