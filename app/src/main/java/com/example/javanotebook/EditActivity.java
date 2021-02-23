package com.example.javanotebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.javanotebook.adapter.ListItem;
import com.example.javanotebook.db.MyConstants;
import com.example.javanotebook.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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

    private boolean isEditState = true;
    private ImageButton imEditImage, imDeleteImage;

    // выносим элемент на уровень класса чтобы для update достать ID
    private ListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntents();
    }

    private void init() {
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        imNewImage = findViewById(R.id.imNewImage);
        imageContainer = findViewById(R.id.imageContainer);
        myDbManager = new MyDbManager(this);
        fbAddImage = findViewById(R.id.fbAddImage);
        imEditImage = findViewById(R.id.imEditImage);
        imDeleteImage = findViewById(R.id.imDeleteImage);
    }

    private void getMyIntents() {
        Intent intent = getIntent();
        if(intent != null) {
            // получаем наш item
            item = (ListItem) intent.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = intent.getBooleanExtra(MyConstants.EDIT_STATE, true);

            // проверяем если isEditState = false, то при клике на элемент
            // списка показывает сам объект(title и description)
            if (!isEditState) {
                edTitle.setText(item.getTitle());
                edDesc.setText(item.getDesc());

                // проверяет если ссылка на картинку не empty (то есть картинка есть),
                // то показываем картинку
                if(!item.getUri().equals("empty")) {
                    // tempUri = item.getUri(); - для того чтобы при update
                    // картинка не оказалась пустой а отсталась такой же
                    tempUri = item.getUri();
                    imageContainer.setVisibility(View.VISIBLE);
                    // Uri.parse() - превращает String в URI
                    imNewImage.setImageURI(Uri.parse(item.getUri()));

                    // убираем кнопки
//                    imDeleteImage.setVisibility(View.GONE);
//                    imEditImage.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        /*Открывает подключение к БД (можно сделать в onCreate())*/
        super.onResume();
        myDbManager.openDb();
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Проверяет чтобы код результата и код запроса на поиск изображения
        // и данные(само изображения) были в порядке, в таком случает
        // показывает выбранное изображения в imageView
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE && data != null) {
            // получаем ссылку на фотографию
            tempUri = data.getData().toString();
            imNewImage.setImageURI(data.getData());
            // ссылку на фотографию, которую мы получили из системы, делаем постоянной
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public void onClickSave(View view) {
        String title = edTitle.getText().toString();
        String desc = edDesc.getText().toString();

        if (title.equals("") || desc.equals("")) {
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            // проверка на сохранение объекта: если сохрняется новый объект,
            // то добавить его в БД, если изменяется старый, то обновить его
            if (isEditState){
                myDbManager.insertToDb(title, desc, tempUri);
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            } else {
                myDbManager.updateItem(item.getId(), title, desc, tempUri);
                Toast.makeText(this, R.string.update, Toast.LENGTH_SHORT).show();
            }
            myDbManager.closeDb();
            finish();
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
        // ACTION_OPEN_DOCUMENT - получает постоянную ссылку, а ACTION_PICK - временную
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.setType("image/*");
        startActivityForResult(chooser, PICK_IMAGE_CODE);
    }


}