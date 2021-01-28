package com.sangmin.LookForClothes;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyPageActivity";

    private FirebaseAuth firebaseAuth;

    private Button btn_logout;
    private Button btn_changepassword;
    private Button btn_delete;
    private Button btn_keep;
    private Button btn_all;
    ListView listView;
    String name, size, color;
    String output;
    String[] index;
    int def = 0;
    ArrayList<String> arraylist;
    String[] store;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        btn_logout = findViewById(R.id.btn_logout);
        btn_changepassword = findViewById(R.id.btn_changepassword);
        btn_delete = findViewById(R.id.btn_delete);
        btn_keep = findViewById(R.id.btn_keep);

        btn_logout.setOnClickListener(this);
        btn_changepassword.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_keep.setOnClickListener(this);


        arraylist = new ArrayList<>();
        store = new String[arraylist.size()];
    }

    @Override
    public void onClick(View v) {
        if(v==btn_logout) {
            firebaseAuth.signOut();
            finish();
            Toast.makeText(MyPageActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(v==btn_changepassword) {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        }

        if(v==btn_keep) {
            listView = (ListView) findViewById(R.id.listView);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_single_choice, arraylist);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            Intent intent = getIntent();
            name = intent.getStringExtra("DATA3");
            Intent intent2 = getIntent();
            size = intent2.getStringExtra("SIZE3");
            Intent intent3 = getIntent();
            color = intent3.getStringExtra("COLOR3");

            if (name == null) {
                output = "";
            }
            else {
                output = name + " / " + size + " / " + color;
                arraylist.add(output);
            }

            SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
            String text = sf.getString("output", "");
            arraylist.add(text);
        }
        if(v==btn_delete) {
            int pos;
            boolean val;
            pos = listView.getCheckedItemPosition();
            val = listView.isItemChecked(pos);
            if (val == true) {
                SharedPreferences test = getSharedPreferences("sFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = test.edit();
                editor.remove("sFile");
                editor.commit();

                arraylist.remove(pos);
                listView.clearChoices();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("output", output);
        editor.commit();
    }
}

