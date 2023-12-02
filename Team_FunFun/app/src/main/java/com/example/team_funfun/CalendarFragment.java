package com.example.team_funfun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private TodoAdapter todoAdapter;

    private List<Todo> todoList;
    private RecyclerView todoRecyclerView;
    private View lastClickedCell = null;
    private List<String[]> todoDataList; // string[5] = { todo, date, state, category, color }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        monthYearText = view.findViewById(R.id.monthYearTV);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        todoRecyclerView = view.findViewById(R.id.todoRecyclerView);
        mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;

        todoDataList = mainActivity.getTodoData();
        selectedDate = LocalDate.now();
        setMonthView();

        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList, mainActivity.getSupportFragmentManager(), mainActivity);

        todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todoRecyclerView.setAdapter(todoAdapter);

        Button previousButton = view.findViewById(R.id.previousButton);
        Button nextButton = view.findViewById(R.id.nextButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoDataList = mainActivity.getTodoData();
                todoList.clear();
                // todoAdapter = new TodoAdapter(todoList, mainActivity.getSupportFragmentManager(), mainActivity);
                // todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                // todoRecyclerView.setAdapter(todoAdapter);
                todoAdapter.notifyItemInserted(todoList.size() - 1);
                todoAdapter.notifyDataSetChanged(); // 데이터 갱신
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoDataList = mainActivity.getTodoData();
                todoList.clear();
                //todoAdapter = new TodoAdapter(todoList, mainActivity.getSupportFragmentManager(), mainActivity);
                //todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                //todoRecyclerView.setAdapter(todoAdapter);
                todoAdapter.notifyItemInserted(todoList.size() - 1);
                todoAdapter.notifyDataSetChanged(); // 데이터 갱신
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        return view;
    }


    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, todoDataList, this, selectedDate); // 수정
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월");
        return date.format(formatter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    @Override
    public void OnItemClick(int position, String dayText) {
        todoList.clear();
        todoAdapter.notifyDataSetChanged();
        todoDataList = mainActivity.getTodoData();
        if (!dayText.equals("")) {
            String msg = monthYearFromDate(selectedDate) + " " + dayText + " 선택했음.";
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

            // 이전에 클릭한 셀의 테두리 제거
            if (lastClickedCell != null) {
                lastClickedCell.setBackgroundResource(0); // 0은 아무 테두리도 없는 리소스 ID
            }

            // 새로 클릭한 셀에 테두리 추가
            View clickedCell = calendarRecyclerView.getLayoutManager().findViewByPosition(position);
            if (clickedCell != null) {
                clickedCell.setBackgroundResource(R.drawable.cell_bg);
                lastClickedCell = clickedCell;
            }
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String selected = selectedDate.format(dateFormatter);
        String day = dayText.trim();
        if(day.length() == 1) day = String.format("%02d", Integer.parseInt(day));
        for(String[] str : todoDataList) {

            String todoContent = str[0];
            Date date = null;
            try {
                date = formatter.parse(str[1]); // string -> Date 객체로 변환
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            int state = Integer.parseInt(str[2]);
            String category = str[3];
            int id = Integer.parseInt(str[5]);

            if(selected.substring(0,7).equals(str[1].substring(0,7))) { //

                if(day.equals(str[1].substring(8,10))) {
                    System.out.println("id= " + id);
                    todoList.add(new Todo(todoContent, date, state, category, id));
                }

            }

        }

        // todoAdapter = new TodoAdapter(todoList, mainActivity.getSupportFragmentManager(), mainActivity);
        // todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // todoRecyclerView.setAdapter(todoAdapter);
        todoAdapter.notifyItemInserted(todoList.size() - 1);
        todoAdapter.notifyDataSetChanged(); // 데이터 갱신
    }


}
