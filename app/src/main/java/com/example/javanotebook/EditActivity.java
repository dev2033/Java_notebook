package com.example.javanotebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.javanotebook.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity {

    // данная константа нужна для запроса интента на поиск изображений в системе
    private final int PICK_IMAGE_CODE = 123;

    // empty нужен для считывания данных из БД, если нет ссылки на изображение,
    // то imageView закрывается и показывается остальной контент без изображения
    private String tempUri = "empty";

    private EditText edTitle, edDesc;
    private MyDbManager myDbManager;
    private ConstraintLayout imageContainer;
    private FloatingActionButton fbAddImage;
    private ImageView imNewImage;
//    private ImageButton imEditImage, imDeleteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init() {
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        imNewImage = findViewById(R.id.imNewImage);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Проверяет чтобы код результата и код запроса на поиск изображения
        // и данные(само изображения) были в порядке, в таком случает
        // показывает выбранное изображения в imageView
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE && data != null) {
            tempUri = data.getData().toString();
            imNewImage.setImageURI(data.getData());
        }
    }

    public void onClickSave(View view) {
        String title = edTitle.getText().toString();
        String desc = edDesc.getText().toString();

        if (title.equals("") || desc.equals("")) {
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            myDbManager.insertToDb(title, desc, tempUri);
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            finish();
            myDbManager.closeDb();
        }
    }

    public void onClickDeleteImage(View view) {
        /*Удаляет изображение + убирает imageContainer с экрана*/
        imNewImage.setImageResource(R.drawable.ic_image_def);
        tempUri = "empty";
        imageContainer.setVisibility(View.GONE);
        fbAddImage.setVisibility(View.VISIBLE);
    }

    public void onClickAddImage(View view) {
        /*Добавляет изображение*/
        imageContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void onClickChooseImage(View view) {
        /*Загружает картинку*/

        // данный Intent ищет все места на смартфоне, где есть изображения
        Intent chooser = new Intent(Intent.ACTION_PICK);
        chooser.setType("image/*");
        startActivityForResult(chooser, PICK_IMAGE_CODE);
    }


}