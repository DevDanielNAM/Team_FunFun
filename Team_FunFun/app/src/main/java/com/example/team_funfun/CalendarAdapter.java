package com.example.team_funfun;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private  List<String[]> todoDataList;  // todoList를 추가
    private final LocalDate selectedDate; // 추가: 선택된 날짜 정보

    public CalendarAdapter(ArrayList<String> daysOfMonth, List<String[]> todoDataList, OnItemListener onItemListener, LocalDate selectedDate) {
        this.daysOfMonth = daysOfMonth;
        this.todoDataList = todoDataList;
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate; // 추가
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)(parent.getHeight() * 0.166677777);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText("    "+daysOfMonth.get(position));

        String day = daysOfMonth.get(position);
        String todo;
        if(day.length() == 1) day = String.format("%02d", Integer.parseInt(day));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = selectedDate.format(formatter).substring(0,7); // 2023-11
        System.out.println("형식화된 날짜: " + formattedDate);

        for(int i=0;i<todoDataList.size();i++) {
            String[] str = todoDataList.get(i);
            if(str[1].substring(0,7).equals(formattedDate)) {
                if(day.equals(str[1].substring(8,10))) {
                    if(str[0].length() > 4) todo= str[0].substring(0,4);
                    else todo = str[0];
                    holder.dayOfToDo.setText(todo);
                    holder.dayOfToDo.setBackgroundColor(Color.parseColor(str[4]));

                }
                //System.out.println(str[0]);

            }
        }


    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }
    public interface OnItemListener {
        void OnItemClick(int position, String dayText);
    }
    public void updateTodoData(List<String[]> updatedTodoData) {
        this.todoDataList = updatedTodoData;
        notifyDataSetChanged(); // 데이터셋이 변경되었음을 알림
    }
}
