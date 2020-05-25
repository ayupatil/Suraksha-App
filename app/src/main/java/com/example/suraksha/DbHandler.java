package com.example.suraksha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {
    //Db Version
    private static final int Db_Version=1;
    //Db Name
    private static final String Db_Name="Suraksha";
    //table name
    private static final String Table_Name="Contact";
    //Creating Columns
    private static final String mynum="mynum";
    private static final String gar1="gar1";
    private static final String gar2="gar2";
    private static final String gar3="gar3";

    //constructor here
    public DbHandler(Context context)
    {
        super(context,Db_Name,null,Db_Version);
    }

    //creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // writing command for sqlite to create table with required columns
        String Create_Table="CREATE TABLE " + Table_Name + "(" + mynum
                + " TEXT," + gar1 + " TEXT," + gar2 + " TEXT," + gar3 + " TEXT" + ")";
        db.execSQL(Create_Table);
    }
    //Upgrading the Db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        //create the table again
        onCreate(db);
    }
    //Add new User by calling this method
    public void addContact(Contact c)
    {

            // getting db instance for writing the user
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(mynum, c.getMynum());
            cv.put(gar1, c.getGar1());
            cv.put(gar2, c.getGar2());
            cv.put(gar3, c.getGar3());

        if(check()==0) {

            //inserting row
            db.insert(Table_Name, null, cv);

        }
        else{
            db.update(Table_Name,cv,null,null);
        }
            //close the database to avoid any leak
            db.close();

    }

    public int check()
    {
        int id = 0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from Contact",null);
        if(cursor.getCount()>0) {
            id = 1;
        }
        return id;
    }


    public Cursor getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from Contact",null);
        return  res;
    }




}
