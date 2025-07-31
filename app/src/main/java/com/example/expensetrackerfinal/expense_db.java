package com.example.expensetrackerfinal;

import android.widget.TableRow;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "expense")
public class expense_db {
    @PrimaryKey
    private int expense_id;
    private String expense_detail;
    private int expense_value;
    private String expense_category;
    private String expense_date;
    private int user_id;
    public int getExpense_id() {return expense_id;}

    public void setExpense_id(int expense_id) {this.expense_id = expense_id;}

    public String getExpense_detail() {
        return expense_detail;
    }

    public void setExpense_detail(String expense_detail) {
        this.expense_detail = expense_detail;
    }

    public int getExpense_value() {
        return expense_value;
    }

    public void setExpense_value(int expense_value) {
        this.expense_value = expense_value;
    }

    public String getExpense_category() {
        return expense_category;
    }

    public void setExpense_category(String expense_category) {
        this.expense_category = expense_category;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }
    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}
    public expense_db() {
    }

    public expense_db(String expense_detail, int expense_value, String expense_category, String expense_date, int userId) {
        this.expense_detail = expense_detail;
        this.expense_value = expense_value;
        this.expense_category = expense_category;
        this.expense_date = expense_date;
        user_id = userId;
    }

}
