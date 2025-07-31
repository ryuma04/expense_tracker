package com.example.expensetrackerfinal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "income")
public class income_db {
    @PrimaryKey
    private int income_id;
    private String income_detail;
    private int income_value;
    private String income_category;
    private String income_date;
    private int user_id;
    public int getIncome_id() {return income_id;}
    public void setIncome_id(int income_id) {this.income_id = income_id;}
    public String getIncome_detail() {
        return income_detail;
    }

    public void setIncome_detail(String income_detail) {
        this.income_detail = income_detail;
    }

    public int getIncome_value() {
        return income_value;
    }

    public void setIncome_value(int income_value) {
        this.income_value = income_value;
    }

    public String getIncome_category() {
        return income_category;
    }

    public void setIncome_category(String income_category) {this.income_category = income_category;}

    public String getIncome_date() {
        return income_date;
    }

    public void setIncome_date(String income_date) {
        this.income_date = income_date;
    }
    public int getUser_id() {return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}
    public income_db(){};
    public income_db( String income_detail, int income_value, String income_category, String income_date, int userId) {
        this.income_detail = income_detail;
        this.income_value = income_value;
        this.income_category = income_category;
        this.income_date = income_date;
        user_id = userId;
    }



}
