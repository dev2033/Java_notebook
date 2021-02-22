package com.example.javanotebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javanotebook.EditActivity;
import com.example.javanotebook.R;
import com.example.javanotebook.db.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private Context context;
    private List<ListItem> mainArray;

    // конструктор класса
    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view, context, mainArray);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // заполнение элементов списка (с 0 позиции)
        // getTitle() - позволяет 'хранить' в title остальные поля, для дальнейшего показа
        holder.setData(mainArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*
        * Данный класс создается каждый раз, когда создается новый элемент.
        * Также он 'слушает' нажатия на каждый элемент списка
        * */
        private Context context;
        private TextView tvTitle;
        private List<ListItem> mainArray;

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            // выбирает данный класс как слушателя нажатий,
            // для того чтобы весь элемент слушал нажатие на экран
            itemView.setOnClickListener(this);
        }

        public void setData(String title) {
            tvTitle.setText(title);
        }

        @Override
        public void onClick(View view) {
            /*'Слушает' нажатия на каждый элемент списка*/
            Intent intent = new Intent(context, EditActivity.class);
            // Передаем mainArray конкретную позицию элемента из
            // класса MainAdapter в класс MyViewHolder
            // для редактирования
            intent.putExtra(MyConstants.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
            // false - для просмотра
            intent.putExtra(MyConstants.EDIT_STATE, false);
            context.startActivity(intent);
        }
    }

    public void updateAdapter(List<ListItem> newList) {
        /*Добавляет элементы в список и перезапускает адаптер*/
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }
}
