package com.example.team_funfun;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class EditTodoFragment extends Fragment {
    private GridLayout categoryContainer;
    Button editTodo;
    Button addCategory;
    Button deleteCategory;
    EditText todoInput;
    EditText dateInput;
    EditText categoryInput;
    ImageButton colorPicker;
    HomeFragment homeFragment;
    String clickedCategory = "";
    String content;
    String date;
    String category;
    int id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_todo, container, false);

        MainActivity mainActivity = (MainActivity)getActivity();
        assert mainActivity != null;

        Bundle args = getArguments();
        if (args != null) {
            content = args.getString("content", "");
            date = args.getString("date", "");
            category = args.getString("category", "");
            id = args.getInt("id", 0);
        }

        homeFragment = new HomeFragment();

        editTodo = rootView.findViewById(R.id.editTodo);
        addCategory = rootView.findViewById(R.id.addCategory);
        deleteCategory = rootView.findViewById(R.id.delteCategory);
        categoryContainer = rootView.findViewById(R.id.categoryContainer);
        todoInput = rootView.findViewById(R.id.todoInput);
        dateInput = rootView.findViewById(R.id.dateInput);
        categoryInput = rootView.findViewById(R.id.categoryInput);
        colorPicker = rootView.findViewById(R.id.colorPicker);

        todoInput.setText(content);
        dateInput.setText(date);
        clickedCategory = category;

        showCalendar(mainActivity, dateInput);

        renderCategoryBtn(mainActivity);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder denyDialog = new AlertDialog.Builder(getContext());

                if(mainActivity.getCategoryCount() > 5) {
                    denyDialog.setTitle("미입력!");
                    denyDialog.setMessage("카테고리는 최대 6개까지 가능합니다!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                } else if(!categoryInput.getText().toString().equals("")) {
                    mainActivity.insertCategoryData(categoryInput.getText().toString(),"#BFFF4141");
                    categoryInput.setText("");
                    renderCategoryBtn(mainActivity);
                } else {
                    denyDialog.setTitle("미입력!");
                    denyDialog.setMessage("추가할 카테고리를 입력해주세요!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                }
            }
        });

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clickedCategory.equals("")) {
                    mainActivity.deleteCategoryData(clickedCategory);
                    renderCategoryBtn(mainActivity);
                } else {
                    AlertDialog.Builder denyDialog = new AlertDialog.Builder(getContext());
                    denyDialog.setTitle("미선택!");
                    denyDialog.setMessage("삭제할 카테고리를 선택해주세요!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                }
            }
        });

        editTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(getContext());
                AlertDialog.Builder denyDialog = new AlertDialog.Builder(getContext());

                if(todoInput.getText().toString().equals("")
                        || dateInput.getText().toString().equals("")
                        || clickedCategory.equals("")) {
                    denyDialog.setTitle("미입력!");
                    denyDialog.setMessage("모든 항목을 입력해주세요!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                } else if(todoInput.getText().toString().equals(content)
                        && dateInput.getText().toString().equals(date)
                        && clickedCategory.equals(category)) {
                    denyDialog.setTitle("변경불가!");
                    denyDialog.setMessage("적어도 하나의 항목을 변경해주세요!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                } else {
                    editDialog.setTitle("Todo 수정");
                    editDialog.setMessage("Todo를 수정하시겠습니까?");
                    editDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    editDialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!todoInput.getText().toString().equals("")
                                    && !dateInput.getText().toString().equals("")
                                    && !clickedCategory.equals("")) {
                                mainActivity.updateTodoData(todoInput.getText().toString(),
                                        Date.valueOf(dateInput.getText().toString()),
                                        0,
                                        clickedCategory,
                                        id);
                            }
                            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                            dialog.dismiss();
                        }
                    });
                    editDialog.show();
                }
            }

        });

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder menu = new AlertDialog.Builder(getContext());
                menu.setTitle("카테고리 색상 선택");

                menu.setView(R.layout.color_picker);

                menu.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                menu.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                menu.show();
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

    private AppCompatButton clickedButton = null;
    // 카테고리 버튼 생성
    private void addCategoryBtn(String category, String color, MainActivity mainActivity) {
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
                // 클릭된 버튼이 있으면 원래 색상으로 돌리기
                if (clickedButton != null) {
                    Drawable unclickedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.unclicked_category_button);
                    GradientDrawable gradientUnclickedDrawable = (GradientDrawable) unclickedDrawable;
                    if (gradientUnclickedDrawable != null) {
                        gradientUnclickedDrawable.setColor(Color.parseColor(mainActivity.getCategoryColor(clickedButton.getText().toString())));
                    }

                    clickedButton.setBackgroundDrawable(unclickedDrawable);
                }

                // 현재 클릭된 버튼 설정
                clickedButton = newCategoryBtn;

                // 현재 클릭된 버튼을 clickedDrawable로 변경
                newCategoryBtn.setBackgroundDrawable(clickedDrawable);
                clickedCategory = newCategoryBtn.getText().toString();
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

    // DB에서 카테고리 항목을 조회해서 카테고리 버튼을 렌더
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