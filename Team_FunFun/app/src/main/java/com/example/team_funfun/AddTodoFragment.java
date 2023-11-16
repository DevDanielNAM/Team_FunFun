package com.example.team_funfun;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTodoFragment extends Fragment {
    private GridLayout categoryContainer;
    Button addTodo;
    Button addCategory;
    Button deleteCategory;
    EditText todoInput;
    EditText dateInput;
    EditText categoryInput;
    boolean isClicked = false;
    String clickedCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_todo, container, false);

        MainActivity mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;

        addTodo = rootView.findViewById(R.id.addTodo);
        addCategory = rootView.findViewById(R.id.addCategory);
        deleteCategory = rootView.findViewById(R.id.delteCategory);
        categoryContainer = rootView.findViewById(R.id.categoryContainer);
        todoInput = rootView.findViewById(R.id.todoInput);
        dateInput = rootView.findViewById(R.id.dateInput);
        categoryInput = rootView.findViewById(R.id.categoryInput);

        showCalendar(mainActivity, dateInput);

        renderCategoryBtn(mainActivity);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryInput.getText().toString().compareTo("") != 0) {
                    mainActivity.insertCategoryData(categoryInput.getText().toString());
                    renderCategoryBtn(mainActivity);
                }
            }
        });

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedCategory != null) {
                    mainActivity.deleteCategoryData(clickedCategory);
                    renderCategoryBtn(mainActivity);
                }
            }
        });

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todoInput.getText().toString().compareTo("") != 0
                && dateInput.getText().toString().compareTo("") != 0
                && clickedCategory.compareTo("") != 0) {
                    mainActivity.insertTodoData(todoInput.getText().toString(),
                            Date.valueOf(dateInput.getText().toString()),
                            clickedCategory,
                            0);
                }
            }
        });
        return rootView;
    }

    private void showCalendar(MainActivity mainActivity, TextView dateInput) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateInput.setText(year + "-" + (month+1) + "-" + (day < 10? "0" + day : day));
            }
        }, year, month, day);

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void addCategoryBtn(String category) {
        AppCompatButton newCategoryBtn = new AppCompatButton(getContext());
        newCategoryBtn.setText(category);
        newCategoryBtn.setAllCaps(false);
        newCategoryBtn.setBackgroundResource(R.drawable.unclicked_category_button);
        newCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClicked) {
                    newCategoryBtn.setBackgroundResource(R.drawable.clicked_category_button);
                    clickedCategory = newCategoryBtn.getText().toString();
                    isClicked = false;
                } else {
                    newCategoryBtn.setBackgroundResource(R.drawable.unclicked_category_button);
                    isClicked = true;
                }
            }
        });
        categoryContainer.addView(newCategoryBtn, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void renderCategoryBtn(MainActivity mainActivity) {
        categoryContainer.removeAllViews();
        List<String> getCategoryList = new ArrayList<>();
        getCategoryList = mainActivity.getCategoryData("Category");
        int count = mainActivity.getCategoryCount("Category");
        for(int i = 0; i<count; i++) {
            addCategoryBtn(getCategoryList.get(i));
        }
    }
}