package com.example.javanotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.javanotebook.adapter.MainAdapter;
import com.example.javanotebook.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity {

    private EditText edTitle, edDesc;
    private MyDbManager myDbManager;
    private ImageView imNewImage;
    private ConstraintLayout imageContainer;
    private FloatingActionButton fbAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init() {
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        imageContainer = findViewById(R.id.imageContainer);
        myDbManager = new MyDbManager(this);
        fbAddImage = findViewById(R.id.fbAddImage);
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

    public void onClickDeleteImage(View view) {
        /*Удаляет изображение + убирает imageContainer с экрана*/
        imageContainer.setVisibility(View.GONE);
        fbAddImage.setVisibility(View.VISIBLE);
    }

    public void onClickAddImage(View view) {
        /*Добавляет изображение*/
        imageContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }


}