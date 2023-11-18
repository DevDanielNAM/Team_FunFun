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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryFragment extends Fragment {
    private LinearLayout categoryContainer;
    boolean isClicked = false;
    String clickedCategory;
    FloatingActionButton floatingAddBtn;
    AddTodoFragment addTodoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;

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

    private void addCategoryBtn(String todo, String date, String state, String category, String color) {
        AppCompatButton newCategoryBtn = new AppCompatButton(getContext());
        TextView todoTextView = new TextView(getContext());

        int textColor = getContrastColor(color);
        int clickedColor = adjustColor(Color.parseColor(color), 0.8f);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_kr_medium);

        newCategoryBtn.setTypeface(typeface);
        newCategoryBtn.setText(category);
        newCategoryBtn.setTextSize(20);
        newCategoryBtn.setTextColor(textColor);
        newCategoryBtn.setAllCaps(false);

        todoTextView.setTypeface(typeface);
        todoTextView.setText(todo);
        todoTextView.setTextSize(20);
        todoTextView.setAllCaps(false);

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
        LinearLayout linearLayout = new LinearLayout(getContext());

        newCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isClicked) {
                        newCategoryBtn.setBackgroundDrawable(clickedDrawable);
                        clickedCategory = newCategoryBtn.getText().toString();
                        linearLayout.addView(todoTextView);
                        isClicked = false;
                    } else {
                        newCategoryBtn.setBackgroundDrawable(unclickedDrawable);
                        linearLayout.removeView(todoTextView);
                        isClicked = true;
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
        categoryContainer.addView(linearLayout, layoutParams);
    }

//    private void addTodo(String todo, String date, String state, String category) {
//        TextView todoTextView = new TextView(getContext());
//
//        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_kr_medium);
//        todoTextView.setTypeface(typeface);
//        todoTextView.setText(todo);
//        todoTextView.setTextSize(20);
//        todoTextView.setAllCaps(false);
//
//    }

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
//        categoryContainer.removeAllViews();

//        List<String[]> getCategoryList = mainActivity.getCategoryData();
//        if (getCategoryList != null) {
//            for (String[] categoryData : getCategoryList) {
//                addCategoryBtn(categoryData[0], categoryData[1]);
//            }
//        }

        List<String[]> getTodoList = mainActivity.getTodoData();
        if (getTodoList != null) {
            for (String[] categoryData : getTodoList) {
                addCategoryBtn(categoryData[0], categoryData[1], categoryData[2], categoryData[3], categoryData[4]);
            }
        }
    }

//    private void renderTodo(MainActivity mainActivity) {
//        List<String[]> getTodoList = mainActivity.getTodoData();
//        if (getTodoList != null) {
//            for (String[] categoryData : getTodoList) {
//                addTodo(categoryData[0], categoryData[1], categoryData[2], categoryData[3]);
//            }
//        }
//    }
}