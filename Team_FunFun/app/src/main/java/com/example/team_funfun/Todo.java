package com.example.team_funfun;

import java.util.Date;

public class Todo {
    private String content;
    private Date date;
    private int state;
    private String category;
    private int id;

    public Todo(String content, Date date, int state, String category, int id) {
        this.content = content;
        this.date = date;
        this.state = state;
        this.category = category;
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public int getState() { return state; }
    public Date getDate() { return date; }
    public String getCategory() { return category; }
    public int getId() { return id; }
    public int setState(int newState) {
        return state = newState;
    }
}

