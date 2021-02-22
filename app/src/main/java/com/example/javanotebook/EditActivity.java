package com.example.javanotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.javanotebook.adapter.MainAdapter;
import com.example.javanotebook.db.MyDbManager;

public class EditActivity extends AppCompatActivity {

    private EditText edTitle, edDesc;
    private MyDbManager myDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init() {
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        myDbManager = new MyDbManager(this);
    }

    @Override
    protected void onResume() {
        /*Открывает подключение к БД (можно сделать в onCreate())*/
        super.onResume();
        myDbManager.openDb();
    }

    public void onClickSave(View view) {
        String title = edTitle.getText().toString();
        String desc = edDesc.getText().toString();

        if (title.equals("") || desc.equals("")) {
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            myDbManager.insertToDb(title, desc);
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            finish();
            myDbManager.closeDb();
        }
    }

}