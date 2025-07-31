package com.example.expensetrackerfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class db_helper extends SQLiteOpenHelper {
    //have to create the variables for table name along with its column so that it can be used further with ease

    //----------------------Used in all tables---------------------------------------
    public static final String USER_ID = "user_id";//obtained from the id column in the users table

    //----------------------For User table-------------------------------------------
    public static final String USER = "user";
    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";

    //---------------------For Income table------------------------------------------
    public static final String INCOME = "INCOME";
    public static final String INCOME_ID = "INCOME_ID";
    public static final String INCOME_DETAIL = "INCOME_DETAIL";
    public static final String INCOME_VALUE = "INCOME_VALUE";
    public static final String INCOME_CATEGORY = "INCOME_CATEGORY";
    public static final String INCOME_DATE = "INCOME_DATE";

    //--------------------For Expense table------------------------------------------
    public static final String EXPENSE = "EXPENSE";
    public static final String EXPENSE_ID = "EXPENSE_ID";
    public static final String EXPENSE_DETAIL = "EXPENSE_DETAIL";
    public static final String EXPENSE_VALUE = "EXPENSE_VALUE";
    public static final String EXPENSE_CATEGORY = "EXPENSE_CATEGORY";
    public static final String EXPENSE_DATE = "EXPENSE_DATE";

    public db_helper(@Nullable Context context) {
        super(context, "expense_tracker.db", null, 1);
    }

    //this method will invoke when the db is created, thus all the table creations statement should be put here
    //make sure after writing the query and passing it to execSQL, you have to select the table and column name>extract>constant>check the constant name>ok(This is done for further easing up the process)
    @Override
    public void onCreate(SQLiteDatabase db) {
        //For user table
        String createUserTable= "CREATE TABLE " + USER + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + EMAIL + " TEXT, " + PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        //For income table
        String createIncomeTable= "CREATE TABLE " + INCOME + "(" + INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + INCOME_DETAIL + " TEXT, " + INCOME_VALUE + " INT, " + INCOME_CATEGORY + " TEXT, " + INCOME_DATE + " DATE, " + USER_ID + " INT)";
        db.execSQL(createIncomeTable);

        //For expense table
        String createExpenseTable= "CREATE TABLE " + EXPENSE + "(" + EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EXPENSE_DETAIL + " TEXT, " + EXPENSE_VALUE + " INT, " + EXPENSE_CATEGORY + " TEXT, " + EXPENSE_DATE + " DATE, " + USER_ID + " INT)";
        db.execSQL(createExpenseTable);
    }

    //this method will be invoked when the db version gets changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //--------------------------------User table methods--------------------------------------------

    //a method for adding data in the user table
    public boolean addUser(user_db udb){
        //for inserting value in the db
        SQLiteDatabase db= this.getWritableDatabase();

        //creating content value to insert data, the first value in the parameter is the coulumn name in the db, and the
        //second value is the value which we will pass to the db. The second valur is obtained from user_db
        ContentValues cv=new ContentValues();
        cv.put(NAME,udb.getName());
        cv.put(EMAIL,udb.getEmail());
        cv.put(PASSWORD,udb.getPassword());

        //adding the data
        long insert = db.insert(USER, null, cv);
        //checking if the data had been inserted successfully or not
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }

    //a function to check whether the user has registered or not, used in login page
    public boolean checkUser(String username, String password){
        boolean userExists;
        SQLiteDatabase db=this.getReadableDatabase();
        String checkQuery = "SELECT * FROM " + USER +
                " WHERE " + NAME + " = '" + username + "'" +
                " AND " + PASSWORD + " = '" + password + "'";

        Cursor c=db.rawQuery(checkQuery,null);
        //this condition will check whether any row exist with that particular name and password
        if(c.getCount()>0){
            userExists= true;
        }
        else{
            userExists=false;
        }
        return userExists;
    }

    //a method to retrieve the user id of the user, after the user logs in
    public int retriveUserID(String name, String password){
        int userID = -1;
        SQLiteDatabase db=this.getReadableDatabase();
        String userRetrival="SELECT " + ID + " FROM " + USER +
                " WHERE " + NAME + " = '" + name + "'" +
                " AND " + PASSWORD + " = '" + password + "'";
        Cursor c=db.rawQuery(userRetrival,null);
        //retrieving the user id from the table
        if(c.moveToFirst()){
            userID=c.getInt(0);
        }
        c.close();
        return userID;
    }

    //-------------------------------Income table methods-----------------------------------------------
    //a method to insert the income in the income db
    public boolean addIncome(income_db idb){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(INCOME_DETAIL, idb.getIncome_detail());
        cv.put(INCOME_VALUE,idb.getIncome_value());
        cv.put(INCOME_CATEGORY, idb.getIncome_category());
        cv.put(INCOME_DATE, idb.getIncome_date());
        cv.put(USER_ID,idb.getUser_id());

        //adding the data
        long insert = db.insert(INCOME, null, cv);
        //checking if data is inserted or not
        if(insert==-1) {
            return false;
        }
        else {
            return true;
        }
    }

    //a method to display the info from income db, will return the data in the form of list of type income_db which i had created for income db
    public List <income_db>displayIncome(String duration, int userID){
        List <income_db> incomeDetail = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        if (duration.equals("Day")) {
            String getDailyIncome = "SELECT * FROM " + INCOME + " WHERE " + USER_ID + " = " + userID + " AND " + INCOME_DATE + " = date('now')";
            Cursor c1= db.rawQuery(getDailyIncome,null);
            //moving to the first row retrieved from the db in cursor
            if(c1.moveToFirst()){
                //looping through each row retieved by the cursor and storing it in the list
                //here do while loop is used to extract the first row also, else using while loop the condition will be checked first ie moveToNext and it will directly move on to the second row without retireving the first row
                do{
                    income_db idb=new income_db();
                    idb.setIncome_id(c1.getInt(c1.getColumnIndexOrThrow("INCOME_ID")));//this method getColumnIndexOrThrow will return t=exception when column name is mispelled or not found
                    idb.setIncome_detail(c1.getString(c1.getColumnIndexOrThrow("INCOME_DETAIL")));
                    idb.setIncome_value(c1.getInt(c1.getColumnIndexOrThrow("INCOME_VALUE")));
                    idb.setIncome_category(c1.getString(c1.getColumnIndexOrThrow("INCOME_CATEGORY")));
                    idb.setIncome_date(c1.getString(c1.getColumnIndexOrThrow("INCOME_DATE")));
                    incomeDetail.add(idb);//adding the data of a row to the list then moving to the next row
                }while(c1.moveToNext());//iterating to the next row in the condition
            }
            c1.close();
            db.close();//closing all the connection
        }
        else if (duration.equals("Week")) {
            //sql query can also be written in this way too...
            String getWeeklyIncome = "SELECT * FROM INCOME WHERE INCOME_DATE >= date('now', '-6 days') " + "AND INCOME_DATE <= date('now') AND USER_ID = " + userID;
            Cursor c2= db.rawQuery(getWeeklyIncome,null);
            if(c2.moveToFirst()){
                do{
                    income_db idb=new income_db();
                    idb.setIncome_id(c2.getInt(c2.getColumnIndexOrThrow("INCOME_ID")));//this method getColumnIndexOrThrow will return t=exception when column name is mispelled or not found
                    idb.setIncome_detail(c2.getString(c2.getColumnIndexOrThrow("INCOME_DETAIL")));
                    idb.setIncome_value(c2.getInt(c2.getColumnIndexOrThrow("INCOME_VALUE")));
                    idb.setIncome_category(c2.getString(c2.getColumnIndexOrThrow("INCOME_CATEGORY")));
                    idb.setIncome_date(c2.getString(c2.getColumnIndexOrThrow("INCOME_DATE")));
                    incomeDetail.add(idb);//adding the data of a row to the list then moving to the next row
                }while(c2.moveToNext());
            }
        }
        else if (duration.equals("Month")) {
            String getMonthlyIncome = "SELECT * FROM INCOME WHERE strftime('%Y-%m', INCOME_DATE) = strftime('%Y-%m', 'now') AND USER_ID = "+userID;
            Cursor c3= db.rawQuery(getMonthlyIncome,null);
            if(c3.moveToFirst()){
                do{
                    income_db idb=new income_db();
                    idb.setIncome_id(c3.getInt(c3.getColumnIndexOrThrow("INCOME_ID")));//this method getColumnIndexOrThrow will return t=exception when column name is mispelled or not found
                    idb.setIncome_detail(c3.getString(c3.getColumnIndexOrThrow("INCOME_DETAIL")));
                    idb.setIncome_value(c3.getInt(c3.getColumnIndexOrThrow("INCOME_VALUE")));
                    idb.setIncome_category(c3.getString(c3.getColumnIndexOrThrow("INCOME_CATEGORY")));
                    idb.setIncome_date(c3.getString(c3.getColumnIndexOrThrow("INCOME_DATE")));
                    incomeDetail.add(idb);//adding the data of a row to the list then moving to the next row
                }while(c3.moveToNext());
            }
        }
        return incomeDetail;
        //the validation for list empty will be done int their respective pages
    }

    //-------------------------------Expense table methods-----------------------------------------------
    //a method to insert the expense in the income db
    public boolean addExpense(expense_db edb){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(EXPENSE_DETAIL, edb.getExpense_detail());
        cv.put(EXPENSE_VALUE, edb.getExpense_value());
        cv.put(EXPENSE_CATEGORY, edb.getExpense_category());
        cv.put(EXPENSE_DATE, edb.getExpense_date());
        cv.put(USER_ID,edb.getUser_id());

        //adding the data
        long insert = db.insert(EXPENSE, null, cv);
        //checking if data is inserted or not
        if(insert==-1) {
            return false;
        }
        else {
            return true;
        }
    }

    //a method to display the info from expense db
    public List <expense_db>displayExpense(String duration, int userID){
        List <expense_db> expenseDetail = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        if (duration.equals("Day")) {
            String getDailyExpense = "SELECT * FROM " + EXPENSE + " WHERE " + USER_ID + " = " + userID + " AND " + EXPENSE_DATE + " = date('now')";
            Cursor e1= db.rawQuery(getDailyExpense,null);
            //moving to the first row retrieved from the db in cursor
            if(e1.moveToFirst()){
                do{
                    expense_db edb=new expense_db();
                    edb.setExpense_id(e1.getInt(e1.getColumnIndexOrThrow("EXPENSE_ID")));//this method getColumnIndexOrThrow will return t=exception when column name is mispelled or not found
                    edb.setExpense_detail(e1.getString(e1.getColumnIndexOrThrow("EXPENSE_DETAIL")));
                    edb.setExpense_value(e1.getInt(e1.getColumnIndexOrThrow("EXPENSE_VALUE")));
                    edb.setExpense_category(e1.getString(e1.getColumnIndexOrThrow("EXPENSE_CATEGORY")));
                    edb.setExpense_date(e1.getString(e1.getColumnIndexOrThrow("EXPENSE_DATE")));
                    expenseDetail.add(edb);//adding the data of a row to the list then moving to the next row
                }while(e1.moveToNext());//iterating to the next row in the condition
            }
            e1.close();
            db.close();//closing all the connection
        }
        else if (duration.equals("Week")) {
            String getWeeklyExpense = "SELECT * FROM EXPENSE WHERE EXPENSE_DATE >= date('now', '-6 days') " + "AND EXPENSE_DATE <= date('now') AND USER_ID = " + userID;
            Cursor e2= db.rawQuery(getWeeklyExpense,null);
            if(e2.moveToFirst()){
                do{
                    expense_db edb=new expense_db();
                    edb.setExpense_id(e2.getInt(e2.getColumnIndexOrThrow("EXPENSE_ID")));//this method getColumnIndexOrThrow will return t=exception when column name is mispelled or not found
                    edb.setExpense_detail(e2.getString(e2.getColumnIndexOrThrow("EXPENSE_DETAIL")));
                    edb.setExpense_value(e2.getInt(e2.getColumnIndexOrThrow("EXPENSE_VALUE")));
                    edb.setExpense_category(e2.getString(e2.getColumnIndexOrThrow("EXPENSE_CATEGORY")));
                    edb.setExpense_date(e2.getString(e2.getColumnIndexOrThrow("EXPENSE_DATE")));
                    expenseDetail.add(edb);//adding the data of a row to the list then moving to the next row
                }while(e2.moveToNext());
            }
        }
        else if (duration.equals("Month")) {
            String getMonthlyExpense = "SELECT * FROM EXPENSE WHERE strftime('%Y-%m', EXPENSE_DATE) = strftime('%Y-%m', 'now') AND USER_ID = "+userID;
            Cursor e3= db.rawQuery(getMonthlyExpense,null);
            if(e3.moveToFirst()){
                do{
                    expense_db edb=new expense_db();
                    edb.setExpense_id(e3.getInt(e3.getColumnIndexOrThrow("EXPENSE_ID")));//this method getColumnIndexOrThrow will return t=exception when column name is mispelled or not found
                    edb.setExpense_detail(e3.getString(e3.getColumnIndexOrThrow("EXPENSE_DETAIL")));
                    edb.setExpense_value(e3.getInt(e3.getColumnIndexOrThrow("EXPENSE_VALUE")));
                    edb.setExpense_category(e3.getString(e3.getColumnIndexOrThrow("EXPENSE_CATEGORY")));
                    edb.setExpense_date(e3.getString(e3.getColumnIndexOrThrow("EXPENSE_DATE")));
                    expenseDetail.add(edb);//adding the data of a row to the list then moving to the next row
                }while(e3.moveToNext());
            }
        }
        return expenseDetail;
        //the validation for list empty will be done int their respective pages
    }

}
