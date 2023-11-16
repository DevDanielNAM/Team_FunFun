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
    SQLiteDatabase dbTodo;
    SQLiteDatabase dbCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String databaseTodo = "Todo";
        String databaseCategory = "Category";

        openDatabase(databaseTodo, databaseCategory);
        createTable(databaseTodo, databaseCategory);
//        deleteDatabase(databaseTodo);
//        deleteDatabase(databaseCategory);
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

    public void openDatabase(String databaseTodo, String databaseCategory){
        println("openDatabase() 호출됨");
        dbTodo = openOrCreateDatabase(databaseTodo, MODE_PRIVATE,null) ; //보안때문에 요즘은 대부분 PRIVATE사용, SQLiteDatabase객체가 반환됨
        if(dbTodo !=null){
            println("Todo 데이터베이스 오픈됨");
        }
        dbCategory = openOrCreateDatabase(databaseCategory, MODE_PRIVATE,null) ; //보안때문에 요즘은 대부분 PRIVATE사용, SQLiteDatabase객체가 반환됨
        if(dbCategory !=null){
            println("Category 데이터베이스 오픈됨");
        }
    }

    public void createTable(String tableName1, String tableName2){
        println("createTodoTable() 호출됨.");

        if(dbTodo!= null) {
            String sql = "create table if not exists " + tableName1 + "(_id integer PRIMARY KEY autoincrement," +
                    " todo text, date datetime, category text, state integer)";
            dbTodo.execSQL(sql);

            println("Todo 테이블 생성됨.");
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }

        if(dbCategory!= null) {
            String sql = "create table if not exists " + tableName2 + "(_id integer PRIMARY KEY autoincrement," +
                    " category text)";
            dbCategory.execSQL(sql);

            println("Category 테이블 생성됨.");
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }
    }

    public void insertTodoData(String todo, Date date, String category, int state){
        println("insertTodoData() 호출됨.");

        if(dbTodo != null && todo.compareTo("") != 0
                && category.compareTo("") != 0
                && date.toString().compareTo("") != 0){
            String sql = "insert into Todo(todo, date, category, state) values(?, ?, ?, ?)";
            Object[] params = {todo, date, category, state};
            dbTodo.execSQL(sql, params);//이런식으로 두번쨰 파라미터로 이런식으로 객체를 전달하면 sql문의 ?를 이 params에 있는 데이터를 물음표를 대체해준다.
            println("Todo 데이터 추가함");
        } else {
            println("데이터베이스를 먼저 오픈하시오");
        }
    }

    public void selectTodoData(String tableName){
        println("selectTodoData() 호출됨.");
        if(dbTodo != null){
            String sql = "select todo, date, category, state from "+tableName;
            Cursor cursor = dbTodo.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            for( int i = 0; i< cursor.getCount(); i++){
                cursor.moveToNext();//다음 레코드로 넘어간다.
                String todo = cursor.getString(0);
                Date date = Date.valueOf(cursor.getString(1));
                String category = cursor.getString(2);
                int state = cursor.getInt(3);
                println("#" + i + " -> " + todo + ", " + date + ", " + category + "," + state );
            }
            cursor.close();
        }
    }
    public void insertCategoryData(String category){
        println("insertCategoryData() 호출됨.");
        if(dbCategory != null && category.compareTo("") != 0
                && isExistCategoryData("Category", category)){
            String sql = "insert into Category(category) values(?)";
            Object[] params = {category};
            dbCategory.execSQL(sql, params);//이런식으로 두번쨰 파라미터로 이런식으로 객체를 전달하면 sql문의 ?를 이 params에 있는 데이터를 물음표를 대체해준다.
            println("데이터 추가함");
        } else {
            println("데이터베이스를 먼저 오픈하시오");
            println("데이터베이스에 이미 존재합니");
        }
    }

    public int getCategoryCount(String tableName){
        println("getCategoryCount() 호출됨.");
        if(dbCategory != null){
            String sql = "select category from "+tableName;
            Cursor cursor = dbCategory.rawQuery(sql, null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        }
        return 0;
    }
    public boolean isExistCategoryData(String tableName, String findCategory){
        println("isExistCategoryData() 호출됨.");
        if(dbCategory != null){
            String sql = "select category from "+tableName;
            Cursor cursor = dbCategory.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            for( int i = 0; i< cursor.getCount(); i++){
                cursor.moveToNext();//다음 레코드로 넘어간다.
                String category = cursor.getString(0);
                if(category.compareTo(findCategory) == 0) {
                    return false;
                }
            }
            cursor.close();
        }
        return true;
    }

    public List<String> getCategoryData(String tableName){
        println("getCategoryData() 호출됨.");
        List<String> categoryList = new ArrayList<>();
        if(dbCategory != null){
            String sql = "select category from "+tableName;
            Cursor cursor = dbCategory.rawQuery(sql, null);
            println("조회된 데이터개수 :" + cursor.getCount());

            for( int i = 0; i< cursor.getCount(); i++){
                cursor.moveToNext();//다음 레코드로 넘어간다.
                String category = cursor.getString(0);
                categoryList.add(category);
            }
            cursor.close();
            return categoryList;
        }
        return null;
    }

    public void deleteCategoryData(String category){
        println("delteCategoryData() 호출됨.");
        if(dbCategory != null && category.compareTo("") != 0){
            String sql = "delete from Category where category = ?";
            Object[] params = {category};
            dbCategory.execSQL(sql, params);//이런식으로 두번쨰 파라미터로 이런식으로 객체를 전달하면 sql문의 ?를 이 params에 있는 데이터를 물음표를 대체해준다.
            println("데이터 삭제함");
        } else {
            println("데이터베이스를 먼저 오픈하시오");
            println("데이터베이스에 없습니다");
        }
    }

    public void println(String data){
        System.out.println(data + "\n");
    }
}

