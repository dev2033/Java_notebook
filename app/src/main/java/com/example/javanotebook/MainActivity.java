package com.example.javanotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.javanotebook.db.MyDbManager;

public class MainActivity extends AppCompatActivity {

    private MyDbManager myDbManager;
    private EditText edTitle, edDesc;
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        tvTest = findViewById(R.id.tvTest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        for(String title : myDbManager.getFromDb()) {
            tvTest.append(title);
            tvTest.append("\n");
        }
    }

    public void onClickSave(View view) {
        tvTest.setText("");
        myDbManager.insertToDb(edTitle.getText().toString(), edDesc.getText().toString());
        for(String title : myDbManager.getFromDb()) {
            tvTest.append(title);
            tvTest.append("\n");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }
}