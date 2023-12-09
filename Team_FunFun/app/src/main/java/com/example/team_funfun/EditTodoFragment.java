package com.example.team_funfun;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class EditTodoFragment extends Fragment {
    private GridLayout categoryContainer;
    private AppCompatButton clickedButton = null;
    private AppCompatButton clickedColorButton = null;
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
    String hexCodeColor = "#FFBF65";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_todo, container, false);

        MainActivity mainActivity = (MainActivity)getActivity();

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
                } else if(categoryInput.getText().toString().length() > 6) {
                    denyDialog.setTitle("글자수 초과!");
                    denyDialog.setMessage("글자수는 6자리까지 가능합니다!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                } else if(!categoryInput.getText().toString().equals("")) {
                    try {
                        mainActivity.insertCategoryData(categoryInput.getText().toString(), hexCodeColor);
                        categoryInput.setText("");
                        Toast.makeText(getContext(), "카테고리가 추가되었습니다!", Toast.LENGTH_LONG).show();
                        renderCategoryBtn(mainActivity);
                    } catch (Exception e) {
                        denyDialog.setTitle("추가 불가!");
                        denyDialog.setMessage("이미 존재하는 카테고리입니다!");
                        denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        denyDialog.show();
                    }
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
                if(clickedCategory.equals("오늘까지")) {
                    AlertDialog.Builder denyDialog = new AlertDialog.Builder(getContext());
                    denyDialog.setTitle("삭제불가!");
                    denyDialog.setMessage("'오늘까지' 카테고리는 삭제할 수 없습니다!");
                    denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    denyDialog.show();
                } else if(!clickedCategory.equals("")) {
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
                            NavigationBarView navigationBarView = mainActivity.findViewById(R.id.bottomNavBar);
                            navigationBarView.setSelectedItemId(R.id.home);
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Todo가 수되었습니다!", Toast.LENGTH_LONG).show();
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

                View colorPickerView = LayoutInflater.from(getContext()).inflate(R.layout.color_picker, null);

                /* color_picker.xml의 colorPicker1 ~ colorPicker9까지의 버튼에 onClickListener 추가 */
                for (int i = 1; i <= 9; i++) {
                    int buttonId = getResources().getIdentifier("colorPicker" + i, "id", requireContext().getPackageName());
                    AppCompatButton colorButton = colorPickerView.findViewById(buttonId);
                    colorButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 클릭된 버튼이 있으면 원래 텍스트로 돌리기
                            if(clickedColorButton != null) {
                                clickedColorButton.setText("");
                            }

                            // 현재 클릭된 버튼 설정
                            clickedColorButton = colorButton;
                            // 현재 클릭된 버튼에 텍스트 추가
                            colorButton.setText("✓");

                            onColorButtonClick(v);
                            Toast.makeText(getContext(), "선택한 색상은 " + hexCodeColor, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                menu.setView(colorPickerView);

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
                        Toast.makeText(getContext(), "색상이 선택되었습니다!", Toast.LENGTH_LONG).show();
                    }
                });

                menu.show();
            }
        });

        return rootView;
    }

    public void onColorButtonClick(View view) {
        Drawable background = view.getBackground();

        int backgroundColor;

        if (background instanceof ColorDrawable) {
            backgroundColor = ((ColorDrawable) background).getColor();
        } else if (background instanceof GradientDrawable) {
            backgroundColor = ((GradientDrawable) background).getColor().getDefaultColor();
        } else {
            backgroundColor = Color.TRANSPARENT;
        }

        // 투명도 포함한 헥사코드로 변환
        hexCodeColor = String.format("#%08X", backgroundColor);
        System.out.println("Selected Color: " + hexCodeColor);
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

    /* 카테고리 버튼 생성 */
    private void addCategoryBtn(String category, String color, MainActivity mainActivity) {
        // 새로운 카테고리 버튼 생성
        AppCompatButton newCategoryBtn = new AppCompatButton(getContext());
        int textColor = getContrastColor(color);
        int clickedColor = adjustColor(Color.parseColor(color), 0.8f);

        // 버튼 속성 설정
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.ibm_plex_sans_kr_light);
        newCategoryBtn.setTypeface(typeface);
        newCategoryBtn.setText(category);
        newCategoryBtn.setTextSize(16);
        newCategoryBtn.setTextColor(textColor);
        newCategoryBtn.setHeight(30);
        newCategoryBtn.setAllCaps(false);

        // 버튼 상태에 따른 Drawable 생성
        Drawable unclickedDrawable = createCategoryButtonDrawable(color);
        Drawable clickedDrawable = createCategoryButtonDrawable(String.format("#%08X", clickedColor));

        // 초기에 unclicked Drawable을 배경으로 설정
        newCategoryBtn.setBackgroundDrawable(unclickedDrawable);

        // 클릭 리스너 설정
        newCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭된 버튼 처리
                handleCategoryButtonClick(newCategoryBtn, clickedDrawable, mainActivity);
            }
        });

        // 레이아웃 파라미터 설정
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int marginInPixels = (int) getResources().getDimension(R.dimen.margin);
        layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

        // 버튼을 categoryContainer에 추가
        categoryContainer.addView(newCategoryBtn, layoutParams);
    }

    /* 카테고리 버튼 Drawable 생성 */
    private Drawable createCategoryButtonDrawable(String color) {
        // unclicked 상태의 Drawable을 가져옴
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.unclicked_category_button);

        if (!TextUtils.isEmpty(color)) {  // 색상이 비어있지 않은 경우
            // GradientDrawable로 변환하여 색상 설정
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            if (gradientDrawable != null) {
                gradientDrawable.setColor(Color.parseColor(color));
            }
        }

        return drawable;
    }

    /* 카테고리 버튼 클릭 처리 */
    private void handleCategoryButtonClick(AppCompatButton button, Drawable clickedDrawable, MainActivity mainActivity) {
        // 이전에 클릭된 버튼이 있으면 원래 색상으로 돌림
        if (clickedButton != null) {
            Drawable unclickedDrawable = createCategoryButtonDrawable(mainActivity.getCategoryColor(clickedButton.getText().toString()));
            clickedButton.setBackgroundDrawable(unclickedDrawable);
        }

        // 현재 클릭된 버튼 설정
        clickedButton = button;

        // 클릭된 상태의 Drawable로 변경
        button.setBackgroundDrawable(clickedDrawable);
        clickedCategory = button.getText().toString();
        mainActivity.getCategoryColor(clickedCategory);
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

    /* DB에서 카테고리 항목을 조회해서 카테고리 버튼을 렌더 */
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