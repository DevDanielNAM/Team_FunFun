package com.example.team_funfun;


import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<Todo> todoList;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;

    EditTodoFragment editTodoFragment;

    public TodoAdapter(List<Todo> todoList, FragmentManager fragmentManager, MainActivity mainActivity) {
        this.todoList = todoList;
        this.fragmentManager = fragmentManager;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_todo_item, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {

        Todo todo = todoList.get(position);

        holder.todoContent.setText(todo.getContent());

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Drawable redDrawable = ContextCompat.getDrawable(mainActivity, R.drawable.red_circle);
        GradientDrawable gradientRedDrawable = (GradientDrawable) redDrawable;
        Drawable yellowDrawable = ContextCompat.getDrawable(mainActivity, R.drawable.yellow_circle);
        GradientDrawable gradientYellowDrawable = (GradientDrawable) yellowDrawable;
        Drawable greenDrawable = ContextCompat.getDrawable(mainActivity, R.drawable.green_circle);
        GradientDrawable gradientGreenDrawable = (GradientDrawable) greenDrawable;

        Date currentDate = new Date();
        final int DAY = 1000 * 60 * 60 * 24;
        int differDay = (int) ((todo.getDate().getTime() - currentDate.getTime()) / DAY);

        if(differDay < 3) {
            holder.deadLine.setBackground(gradientRedDrawable);
        } else if(differDay < 7) {
            holder.deadLine.setBackground(gradientYellowDrawable);
        } else {
            holder.deadLine.setBackground(gradientGreenDrawable);
        }

        if (todo.getState() == 1) {
            holder.todoContent.setPaintFlags(holder.todoContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkedBox.setImageResource(R.drawable.checked_box);
        } else {
            holder.todoContent.setPaintFlags(holder.todoContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.checkedBox.setImageResource(R.drawable.unchecked_box);
        }

        holder.checkedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newState = (todo.getState() == 0) ? 1 : 0;

                todo.setState(newState);

                if (newState == 1) {
                    holder.todoContent.setPaintFlags(holder.todoContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.checkedBox.setImageResource(R.drawable.checked_box);
                    mainActivity.updateTodoState(todo.getId(), 1);
                } else {
                    holder.todoContent.setPaintFlags(holder.todoContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.checkedBox.setImageResource(R.drawable.unchecked_box);
                    mainActivity.updateTodoState(todo.getId(), 0);
                }

                notifyDataSetChanged();
            }
        });

        holder.editTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("content", todo.getContent());
                bundle.putString("date", todo.getDate().toString());
                bundle.putString("category", todo.getCategory());
                bundle.putInt("id", todo.getId());

                editTodoFragment = new EditTodoFragment();
                editTodoFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.container, editTodoFragment).commit();
                notifyDataSetChanged();
            }
        });

        holder.deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.deleteTodoData(todo.getContent());
                todoList.remove(todo);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
