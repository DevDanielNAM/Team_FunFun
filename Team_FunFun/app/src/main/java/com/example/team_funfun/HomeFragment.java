package com.example.team_funfun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    AddTodoFragment addTodoFragment;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private List<Todo> todoList; // 어댑터로 리사이클러 뷰에 보여줄 값
    private List<String[]> todoDataList; // DB에서 가져온 값
    CalendarFragment calendarFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        calendarFragment = new CalendarFragment();

        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList, mainActivity.getSupportFragmentManager(), mainActivity,calendarFragment);

        recyclerView = rootView.findViewById(R.id.todoContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoAdapter);

        //todoDataList = mainActivity.getTodoData();
        addTodoFragment = new AddTodoFragment();
        Button addBtn = rootView.findViewById(R.id.mainAddTodo);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, addTodoFragment).commit();
            }
        });

        return rootView;
    }
}