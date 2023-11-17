package com.example.team_funfun;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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
                    mainActivity.insertCategoryData(categoryInput.getText().toString(),"#BFFF4141");
                    categoryInput.setText("");
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
                if(!todoInput.getText().toString().equals("")
                && !dateInput.getText().toString().equals("")
                && !clickedCategory.equals("")) {
                    mainActivity.insertTodoData(todoInput.getText().toString(),
                            Date.valueOf(dateInput.getText().toString()),
                            0,
                            clickedCategory);
                }
            }
        });

//        mainActivity.updateTodoData(todoInput.getText().toString(),
//                Date.valueOf(dateInput.getText().toString()),
//                0,
//                clickedCategory);

//        List<String[]> getTodoData = mainActivity.getTodoData();
//        for(String[] todoData : getTodoData)
//            System.out.println(todoData[0] + todoData[1] + todoData[2] + todoData[3] + todoData[4]);

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

    // 카테고리 버튼 생성
    private void addCategoryBtn(String category, String color) {
        AppCompatButton newCategoryBtn = new AppCompatButton(getContext());
        int textColor = getContrastColor(color);
        int clickedColor = adjustColor(Color.parseColor(color), 0.8f);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_kr_light);
        newCategoryBtn.setTypeface(typeface);
        newCategoryBtn.setText(category);
        newCategoryBtn.setTextSize(16);
        newCategoryBtn.setTextColor(textColor);
        newCategoryBtn.setHeight(30);
        newCategoryBtn.setAllCaps(false);

        // 전달받은 color를 unclicked 상태의 버튼에 적용
        Drawable unclickedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.unclicked_category_button);
        GradientDrawable gradientUnclickedDrawable = (GradientDrawable) unclickedDrawable;
        if (gradientUnclickedDrawable != null) {
            gradientUnclickedDrawable.setColor(Color.parseColor(color));
        }

        // 전달받은 color를 clicked 상태의 버튼에 적용
        Drawable clickedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clicked_category_button);
        GradientDrawable gradientClickedDrawable = (GradientDrawable) clickedDrawable;
        if (gradientClickedDrawable != null) {
            gradientClickedDrawable.setColor(clickedColor);
        }

        newCategoryBtn.setBackgroundDrawable(unclickedDrawable);

        newCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClicked) {
                    newCategoryBtn.setBackgroundDrawable(clickedDrawable);
                    clickedCategory = newCategoryBtn.getText().toString();
                    isClicked = false;
                } else {
                    newCategoryBtn.setBackgroundDrawable(unclickedDrawable);
                    isClicked = true;
                }
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int marginInPixels = (int) getResources().getDimension(R.dimen.margin);
        layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

        categoryContainer.addView(newCategoryBtn, layoutParams);
    }

    private int adjustColor(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor; // adjust brightness
        return Color.HSVToColor(hsv);
    }

    private int getContrastColor(String backgroundColor) {
        int backgroundRgb = Color.parseColor(backgroundColor);
        int red = Color.red(backgroundRgb);
        int green = Color.green(backgroundRgb);
        int blue = Color.blue(backgroundRgb);

        // Calculate relative luminance using the formula
        double luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255.0;

        // Choose black or white text based on the luminance value
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    // DB에서 카테고리 항목을 조회해서 카테고리 버튼을 렌더
    private void renderCategoryBtn(MainActivity mainActivity) {
        categoryContainer.removeAllViews();
        List<String[]> getCategoryList = mainActivity.getCategoryData();
        if (getCategoryList != null) {
            for (String[] categoryData : getCategoryList) {
                addCategoryBtn(categoryData[0], categoryData[1]);
            }
        }
    }

}