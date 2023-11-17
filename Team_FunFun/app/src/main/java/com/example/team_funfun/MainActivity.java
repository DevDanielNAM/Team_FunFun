package com.example.team_funfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    CalendarFragment calendarFragment;
    CategoryFragment categoryFragment;
    SQLiteDatabase todoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String dbName = "todoDb";
        openDatabase(dbName);
        createTable();

        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        categoryFragment = new CategoryFragment();

        NavigationBarView navigationBarView = findViewById(R.id.bottomNavBar);

        // 초기 Bottom Navigation Bar 아이콘 Home으로 선택
        navigationBarView.setSelectedItemId(R.id.home);

        // Home Fragment로 시작
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        // bottomNavBar로부터 이벤트를 전달받아 ID값에 따른 Fragment 처리
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                } else if(item.getItemId() == R.id.calendar) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
                        return true;
                } else if(item.getItemId() == R.id.category) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, categoryFragment).commit();
                        return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void openDatabase(String dbName){
        println("openDatabase() 호출됨");
        todoDb = openOrCreateDatabase(dbName, MODE_PRIVATE,null) ; //보안때문에 요즘은 대부분 PRIVATE사용, SQLiteDatabase객체가 반환됨
        if(todoDb !=null){
            println("Todo 데이터베이스 오픈됨");
        }
    }

    /* Todo table, Category table 생성 */
    public void createTable(){
        println("createTodoTable() 호출됨.");
        todoDb.execSQL("PRAGMA foreign_keys = ON");
        if(todoDb!= null) {
            String sqlTodo = "create table if not exists Todo(_id integer PRIMARY KEY autoincrement," +
                    " todo text, date datetime, state integer, category text, FOREIGN KEY(category) REFERENCES Category(categoryName) ON DELETE RESTRICT)";
            todoDb.execSQL(sqlTodo);
            println("Todo 테이블 생성됨.");

            String sqlCategory = "create table if not exists Category(categoryName text PRIMARY KEY, color text)";
            todoDb.execSQL(sqlCategory);
            println("Category 테이블 생성됨.");
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }
    }

    /* Todo talbe에 Data 저장 */
    public void insertTodoData(String todo, Date date, int state, String category){
        println("insertTodoData() 호출됨.");

        if(todoDb != null && !todo.equals("") && !category.equals("") && !date.toString().equals("")) {
            String sql = "insert into Todo(todo, date, state, category) values(?, ?, ?, ?)";
            Object[] params = {todo, date, state, category};
            todoDb.execSQL(sql, params);
            println("Todo 데이터 추가함");
        } else {
            println("데이터베이스를 먼저 오픈하시오");
        }
    }

    /* Todo table에 저장된 Data들 조회 */
    public List<String[]> getTodoData(){
        println("getTodoData() 호출됨.");
        List<String[]> todoList = new ArrayList<>();
        if(todoDb != null){
            String sql = "select todo, date, state, category, Category.color from Todo " +
                    "inner join Category on Todo.category = Category.categoryName";
            Cursor cursor = todoDb.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            while (cursor.moveToNext()){
                String todo = cursor.getString(0);
                Date date = Date.valueOf(cursor.getString(1));
                int state = cursor.getInt(2);
                String category = cursor.getString(3);
                String color = cursor.getString(4);
                String[] todoData = {todo, date.toString(), String.valueOf(state), category, color};
                todoList.add(todoData);
            }
            cursor.close();
            return todoList;
        }
        return null;
    }

    /* 전달받은 Data들로 Todo table 수정 */
    public void updateTodoData(String todo, Date date, int state, String category) {
        println("updateTodoData() 호출됨.");

        if(todoDb != null && !todo.equals("") && !category.equals("") && !date.toString().equals("")) {
            String sql = "update Todo set todo = ?, date = ?, state = ?, category = ?";
            Object[] params = {todo, date, state, category};
            todoDb.execSQL(sql, params);
            println("Todo 데이터 수정함");
        } else {
            println("수정할 데이터를 입력하세요");
        }
    }

    /* Todo table에서 전달받은 todo로 데이터 삭제 */
    public void deleteTodoData(String todo){
        println("delteTodoData() 호출됨.");
        try {
            if(todoDb != null && !todo.equals("")){
                String sql = "delete from Todo where todo = ?";
                Object[] params = {todo};
                todoDb.execSQL(sql, params);
                println("데이터 삭제함");
            }
        } catch (Exception e) {
            println(e.toString());
        }
    }

//    public void selectTodoData(){
//        println("selectTodoData() 호출됨.");
//        if(todoDb != null){
//            String sql = "select todo, date, state, category, Category.color from Todo " +
//                    "inner join Category on Todo.category = Category.categoryName";
//            Cursor cursor = todoDb.rawQuery(sql, null);
//            println("조회된 데이터개수 :" + cursor.getCount());
//            int i = 0;
//            while (cursor.moveToNext()){
//                i++;
//                String todo = cursor.getString(0);
//                Date date = Date.valueOf(cursor.getString(1));
//                int state = cursor.getInt(2);
//                String category = cursor.getString(3);
//                String color = cursor.getString(4);
//                println("#" + i + " -> " + todo + ", " + date + ", " + category + "," + state + "," + color);
//            }
//            cursor.close();
//        }
//    }

//    public boolean isTodoExistCategory(String findCategory){
//        println("selectTodoData() 호출됨.");
//        if(todoDb != null){
//            String sql = "select category from Todo";
//            Cursor cursor = todoDb.rawQuery(sql, null);
//            println("조회된 데이터개수 :" + cursor.getCount());
//
//            while (cursor.moveToNext()){
//                String category = cursor.getString(0);
//                if(category.equals(findCategory)) {
//                    return true;
//                }
//            }
//            cursor.close();
//        }
//        return false;
//    }
    /* Category table에 Data 저장 */
    public void insertCategoryData(String category, String color){
        println("insertCategoryData() 호출됨.");
        try {
            if(todoDb != null && !category.equals("")){
                String sql = "insert into Category(categoryName, color) values(?, ?)";
                Object[] params = {category, color};
                todoDb.execSQL(sql, params);
                println("데이터 추가함");
            }
        } catch (Exception e) {
            println(e.toString());
        }

    }

//    public int getCategoryCount(){
//        println("getCategoryCount() 호출됨.");
//        if(todoDb != null){
//            String sql = "select categoryName from Category";
//            Cursor cursor = todoDb.rawQuery(sql, null);
//            int cnt = cursor.getCount();
//            cursor.close();
//            return cnt;
//        }
//        return 0;
//    }

//    public boolean isExistCategoryData(String findCategory){
//        println("isExistCategoryData() 호출됨.");
//        if(todoDb != null){
//            String sql = "select categoryName from Category";
//            Cursor cursor = todoDb.rawQuery(sql, null);
//            println("조회된 데이터개수 :" + cursor.getCount());
//
//            while(cursor.moveToNext()) {
//                String category = cursor.getString(0);
//                if(category.equals(findCategory)) {
//                    return false;
//                }
//            }
//            cursor.close();
//        }
//        return true;
//    }

    /* Category table에 저장된 Data들 조회 */
    public List<String[]> getCategoryData(){
        println("getCategoryData() 호출됨.");
        List<String[]> categoryList = new ArrayList<>();
        if(todoDb != null){
            String sql = "select categoryName, color from Category";
            Cursor cursor = todoDb.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            while(cursor.moveToNext()) {
                String category = cursor.getString(0);
                String color = cursor.getString(1);
                String[] categoryData = {category, color};
                categoryList.add(categoryData);
            }
            cursor.close();
            return categoryList;
        }
        return null;
    }

    /* Category table에서 전달받은 category를 삭제 */
    public void deleteCategoryData(String category){
        println("delteCategoryData() 호출됨.");
        try {
            if(todoDb != null && !category.equals("")){
                String sql = "delete from Category where categoryName = ?";
                Object[] params = {category};
                todoDb.execSQL(sql, params);
                println("데이터 삭제함");
            }
        } catch (Exception e) {
            println(e.toString());
        }
    }

    public void println(String data){
        System.out.println(data + "\n");
    }
}

