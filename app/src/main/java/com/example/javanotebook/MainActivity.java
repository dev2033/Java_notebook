package com.example.javanotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Редактироване меню, добален поиск по элементам списка*/
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv = (SearchView) item.getActionView();
        // setOnQueryTextListener - слушатель нажатий
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // данная функция делает поиск только по нажатию на клавишу поиск на клавиатуре
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // данная функция делает поиск сразу по введенным данным в строку поиска
            @Override
            public boolean onQueryTextChange(String newText) {
                mainAdapter.updateAdapter(myDbManager.getFromDb(newText));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        /*Инициализирует компоненты*/
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(rcView);
        rcView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {

        super.onResume();
        myDbManager.openDb();
        // запрос идет по всем словам, а функции onQueryTextChange(), по буквам
        mainAdapter.updateAdapter(myDbManager.getFromDb(""));
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

    private ItemTouchHelper getItemTouchHelper() {
        /*
        * Свайп по элементу (RecyclerView) списка с записями (ЛЕВО и ПРАВО)
        * */
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /*
                * Удаление элементов по свайпу
                * */
                // передаем сюда функцию адаптера и сам менеджер
                // через viewHolder мы передается id элемента
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDbManager);
            }
        });
    }
}