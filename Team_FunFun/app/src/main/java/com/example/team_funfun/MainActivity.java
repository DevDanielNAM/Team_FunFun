package com.example.team_funfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
            String sqlCategory = "CREATE TABLE IF NOT EXISTS Category(categoryName text PRIMARY KEY, color text NOT NULL)";
            todoDb.execSQL(sqlCategory);
            println("Category 테이블 생성됨.");

            String sqlTodo = "CREATE TABLE IF NOT EXISTS Todo(_id integer PRIMARY KEY autoincrement," +
                    " todo text NOT NULL, date datetime NOT NULL, state integer NOT NULL, category text NOT NULL, FOREIGN KEY(category) REFERENCES Category(categoryName) ON DELETE RESTRICT)";
            todoDb.execSQL(sqlTodo);
            println("Todo 테이블 생성됨.");

            if (isTableEmpty("Category")) {
                todoDb.execSQL("INSERT INTO Category (categoryName, color) VALUES ('오늘까지', '#BFFF4141')");
                todoDb.execSQL("INSERT INTO Category (categoryName, color) VALUES ('할 일', '#8AC0E7')");
                todoDb.execSQL("INSERT INTO Category (categoryName, color) VALUES ('스터디', '#EB89A4FF')");
            }

            if (isTableEmpty("Todo")) {
                todoDb.execSQL("INSERT INTO Todo (todo, date, state, category) VALUES ('OSS 온라인 강의', '2023-12-01', 0, '할 일')");
                todoDb.execSQL("INSERT INTO Todo (todo, date, state, category) VALUES ('자료구조 과제', '2023-12-03', 0, '할 일')");
                todoDb.execSQL("INSERT INTO Todo (todo, date, state, category) VALUES ('알고리즘', '2023-12-23', 0, '스터디')");
            }
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }
    }

    private boolean isTableEmpty(String tableName) {
        Cursor cursor = todoDb.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    /* Todo talbe에 Data 저장 */
    public void insertTodoData(String todo, Date date, int state, String category){
        println("insertTodoData() 호출됨.");

        try {
            if(todoDb != null && !todo.equals("") && !category.equals("") && !date.toString().equals("")) {
                String sql = "INSERT INTO Todo(todo, date, state, category) VALUES(?, ?, ?, ?)";
                Object[] params = {todo, date, state, category};
                todoDb.execSQL(sql, params);
                println("Todo 데이터 추가함");
            }
        } catch (Exception e) {
            println(e.toString());
        }
    }

    /* Todo table에 저장된 Data들 조회 */
    public List<String[]> getTodoData(){
        println("getTodoData() 호출됨.");
        List<String[]> todoList = new ArrayList<>();
        if(todoDb != null){
            String sql = "select todo, date, state, category, Category.color, _id from Todo " +
                    "inner join Category on Todo.category = Category.categoryName";
            Cursor cursor = todoDb.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            while (cursor.moveToNext()){
                String todo = cursor.getString(0);
                Date date = Date.valueOf(cursor.getString(1));
                int state = cursor.getInt(2);
                String category = cursor.getString(3);
                String color = cursor.getString(4);
                int id = cursor.getInt(5);
                String[] todoData = {todo, date.toString(), String.valueOf(state), category, color, String.valueOf(id)};
                todoList.add(todoData);
            }
            cursor.close();
            return todoList;
        }
        return null;
    }

    /* Todo table에서 전달받은 category에 해당하는 모든 Data들을 조회 */
    public List<String[]> getTodoData(String paramCategory){
        println("getTodoData() 호출됨.");
        List<String[]> todoList = new ArrayList<>();
        if(todoDb != null){
            String sql = "SELECT todo, date, state, category, _id FROM Todo WHERE category = " + "'" + paramCategory + "'";
            Cursor cursor = todoDb.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            while (cursor.moveToNext()){
                String todo = cursor.getString(0);
                Date date = Date.valueOf(cursor.getString(1));
                int state = cursor.getInt(2);
                String category = cursor.getString(3);
                int id = cursor.getInt(4);
                String[] todoData = {todo, date.toString(), String.valueOf(state), category, String.valueOf(id)};
                todoList.add(todoData);
                println(todo + date + state + category + id);
            }
            cursor.close();
            return todoList;
        }
        return null;
    }

    /* 입력받은 Date에 해당하는 Todo 검색 */
    public List<String[]> getTodoData(Date paramDate){
        println("getTodoData(paramDate) 호출됨.");
        List<String[]> todoList = new ArrayList<>();
        if(todoDb != null){
            String sql = "SELECT todo, date, state, category, _id FROM Todo WHERE date = " + "'" + paramDate + "'";
            Cursor cursor = todoDb.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            while (cursor.moveToNext()){
                String todo = cursor.getString(0);
                Date date = Date.valueOf(cursor.getString(1));
                int state = cursor.getInt(2);
                String category = cursor.getString(3);
                int id = cursor.getInt(4);
                String[] todoData = {todo, date.toString(), String.valueOf(state), category, String.valueOf(id)};
                todoList.add(todoData);
                println(todo + date + state + category + id);
            }
            cursor.close();
            return todoList;
        }
        return null;
    }

    /* 전달받은 Data들로 Todo table 수정 */
    public void updateTodoData(String todo, Date date, int state, String category, int _id) {
        println("updateTodoData() 호출됨.");
        try {
            if(todoDb != null && !todo.equals("") && !category.equals("") && !date.toString().equals("")) {
                String sql = "UPDATE Todo SET todo = ?, date = ?, state = ?, category = ? WHERE _id = ?";
                Object[] params = {todo, date, state, category, _id};
                todoDb.execSQL(sql, params);
                println("Todo 데이터 수정함");
            }
        } catch (Exception e) {
            println(e.toString());
            println("수정할 데이터를 입력하세요");
        }
    }

    /* Todo table에서 체크 여부에 따른 state 변경 */
    public void updateTodoState(int id, int state) {
        println("updateTodoState() 호출됨.");
        try {
            if(todoDb != null) {
                String sql = "UPDATE Todo SET state = ? WHERE _id = ?";
                Object[] params = {state, id};
                todoDb.execSQL(sql, params);
                println(id + "," + state);
                println("Todo state 수정함");
            }
        } catch (Exception e) {
            println(e.toString());
            println("수정할 데이터를 입력하세요");
        }
    }

    /* Todo table에서 전달받은 todo로 데이터 삭제 */
    public void deleteTodoData(String todo){
        println("delteTodoData() 호출됨.");
        try {
            if(todoDb != null && !todo.equals("")){
                String sql = "DELETE FROM Todo WHERE todo = ?";
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

    /* Category table에 Data 저장 */
    public void insertCategoryData(String category, String color) throws Exception {
        println("insertCategoryData() 호출됨.");
        if(todoDb != null && !category.equals("")){
            String sql = "INSERT INTO Category(categoryName, color) VALUES(?, ?)";
            Object[] params = {category, color};
            todoDb.execSQL(sql, params);
            println("데이터 추가함");
        }

        throw new Exception();
    }

    /* Category table에 저장된 category 개수 조회 */
    public int getCategoryCount(){
        println("getCategoryCount() 호출됨.");
        if(todoDb != null){
            String sql = "SELECT categoryName FROM Category";
            Cursor cursor = todoDb.rawQuery(sql, null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        }
        return 0;
    }

    /* Category table에서 전달받은 categroy로 color 검색 */
    public String getCategoryColor(String category){
        println("getCategoryColor() 호출됨.");
        if(todoDb != null){
            String sql = "SELECT color FROM Category WHERE categoryName = " + "'" + category + "'";
            Cursor cursor = todoDb.rawQuery(sql, null);
            String color = "";
            while (cursor.moveToNext()){
                color = cursor.getString(0);
            }
            cursor.close();
            println(color);
            return color;
        }
        return null;
    }

    /* Category table에 저장된 Data들 조회 */
    public List<String[]> getCategoryData(){
        println("getCategoryData() 호출됨.");
        List<String[]> categoryList = new ArrayList<>();
        if(todoDb != null){
            String sql = "SELECT categoryName, color FROM Category";
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
                String sql = "DELETE FROM Category WHERE categoryName = ?";
                Object[] params = {category};
                todoDb.execSQL(sql, params);
                println("데이터 삭제함");
            }
        } catch (Exception e) {
            println(e.toString());
            AlertDialog.Builder denyDialog = new AlertDialog.Builder(this);

            denyDialog.setTitle("카테고리 삭제 불가!");
            denyDialog.setMessage("Todo에 사용 중인 카테고리입니다!");
            denyDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            denyDialog.show();

        }
    }

    public void println(String data){
        System.out.println(data + "\n");
    }
}

