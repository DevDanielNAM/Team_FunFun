package com.example.team_funfun;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    ImageButton checkedBox;
    TextView todoContent;
    ImageButton editTodo;
    ImageButton deleteTodo;
    TextView deadLine;

    TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        checkedBox = itemView.findViewById(R.id.checkedBox);
        todoContent = itemView.findViewById(R.id.todoContent);
        editTodo = itemView.findViewById(R.id.editTodo);
        deleteTodo = itemView.findViewById(R.id.deleteTodo);
        deadLine = itemView.findViewById(R.id.deadLine);
    }
}
