package com.example.team_funfun;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private GridLayout categoryContainer;
    private AppCompatButton clickedButton = null;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private List<Todo> todoList;
    FloatingActionButton floatingAddBtn;
    AddTodoFragment addTodoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList, mainActivity.getSupportFragmentManager(), mainActivity);

        recyclerView = rootView.findViewById(R.id.todoContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoAdapter);

        addTodoFragment = new AddTodoFragment();

        categoryContainer = rootView.findViewById(R.id.categoryContainer);
        floatingAddBtn = rootView.findViewById(R.id.floatingButton);

        floatingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, addTodoFragment).commit();
            }
        });

        renderCategoryBtn(mainActivity);

        return rootView;
    }

    /* 카테고리 버튼 생성 */
    private void addCategoryBtn(String category, String color, MainActivity mainActivity) {
        AppCompatButton newCategoryBtn = new AppCompatButton(getContext());

        int textColor = getContrastColor(color);
        int clickedColor = adjustColor(Color.parseColor(color), 0.8f);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_kr_medium);

        newCategoryBtn.setTypeface(typeface);
        newCategoryBtn.setText(category);
        newCategoryBtn.setTextSize(20);
        newCategoryBtn.setTextColor(textColor);
        newCategoryBtn.setAllCaps(false);

        Drawable unclickedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.unclicked_category_button);
        GradientDrawable gradientUnclickedDrawable = (GradientDrawable) unclickedDrawable;
        if (gradientUnclickedDrawable != null) {
            gradientUnclickedDrawable.setColor(Color.parseColor(color));
        }

        Drawable clickedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clicked_category_button);
        GradientDrawable gradientClickedDrawable = (GradientDrawable) clickedDrawable;
        if (gradientClickedDrawable != null) {
            gradientClickedDrawable.setColor(clickedColor);
        }

        newCategoryBtn.setBackgroundDrawable(unclickedDrawable);

        newCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 클릭된 버튼이 있으면 원래 색상으로 돌리기
                    if (clickedButton != null) {
                        Drawable unclickedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.unclicked_category_button);
                        GradientDrawable gradientUnclickedDrawable = (GradientDrawable) unclickedDrawable;
                        if (gradientUnclickedDrawable != null) {
                            gradientUnclickedDrawable.setColor(Color.parseColor(mainActivity.getCategoryColor(clickedButton.getText().toString())));
                        }

                        clickedButton.setBackgroundDrawable(unclickedDrawable);
                        todoList.clear();
                        todoAdapter.notifyDataSetChanged();
                    }
                        // 현재 클릭된 버튼 설정
                        clickedButton = newCategoryBtn;

                        // 현재 클릭된 버튼을 clickedDrawable로 변경
                        newCategoryBtn.setBackgroundDrawable(clickedDrawable);
                        List<String[]> todoList;

                        if(newCategoryBtn.getText().toString().equals("오늘까지")) {
                            java.util.Date currentDate = new java.util.Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date todayDate = Date.valueOf(formatter.format(currentDate));
                            todoList = mainActivity.getTodoData(todayDate);
                        } else {
                            todoList = mainActivity.getTodoData(category);
                        }

                        for (String[] todoData : todoList) {
                            addTodoItem(todoData[0],                // todo content
                                    Date.valueOf(todoData[1]),      // date
                                    Integer.parseInt(todoData[2]),  // state
                                    category,                       // category
                                    Integer.parseInt(todoData[4])); // _id

                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
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

    private void addTodoItem(String content, Date date , int state, String category, int id) {
        Todo todo = new Todo(content, date, state, category, id);
        todoList.add(todo);
        todoAdapter.notifyItemInserted(todoList.size() - 1);
    }

    private int adjustColor(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    private int getContrastColor(String backgroundColor) {
        int backgroundRgb = Color.parseColor(backgroundColor);
        int red = Color.red(backgroundRgb);
        int green = Color.green(backgroundRgb);
        int blue = Color.blue(backgroundRgb);
        double luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255.0;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    private void renderCategoryBtn(MainActivity mainActivity) {
        categoryContainer.removeAllViews();
        List<String[]> getCategoryList = mainActivity.getCategoryData();
        if (getCategoryList != null) {
            for (String[] categoryData : getCategoryList) {
                addCategoryBtn(categoryData[0], categoryData[1], mainActivity);
            }
        }
    }
}
