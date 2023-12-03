package com.example.team_funfun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        Date today = new Date(System.currentTimeMillis());
        todoDataList = mainActivity.getTodoData(today);
        System.out.println("오늘 일정 >> " + todoDataList.get(0)[0]);

        for (String[] str : todoDataList) { // db에서 가져온 투두리스트
            String todoContent = str[0];
            java.util.Date date = java.sql.Date.valueOf(str[1]);
            int state = Integer.parseInt(str[2]);
            String category = str[3];
            int id = Integer.parseInt(str[4]);
            todoList.add(new Todo(todoContent, date, state, category, id)); // db에서 가져온 투두리스트를 화면에 보여주기위함
        }

        todoAdapter.notifyItemInserted(todoList.size() - 1);
        todoAdapter.notifyDataSetChanged(); // 데이터 갱신

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