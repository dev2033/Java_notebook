package com.example.javanotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.javanotebook.adapter.MainAdapter;
import com.example.javanotebook.db.MyDbManager;

public class MainActivity extends AppCompatActivity {

    private MyDbManager myDbManager;
    private EditText edTitle, edDesc;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        /*Инициализирует компоненты*/
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        mainAdapter.updateAdapter(myDbManager.getFromDb());
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }
}