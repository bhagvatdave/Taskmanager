package com.example.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Tasks;

public class MyDatabase extends SQLiteOpenHelper {
    Context context;
    public MyDatabase(@Nullable Context context) {
        super(context, "Mydb", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table Tasks(id integer primary key autoincrement, title text not null,description text not null,flag text,pushpin integer,datecreate text,dateupdate text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertData(Tasks t){
        SQLiteDatabase sd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", t.getTitle());
        cv.put("description", t.getDescription());
        cv.put("flag", t.getFlag());
        cv.put("pushpin",0);
        cv.put("datecreate", t.getDatecreated());
        cv.put("dateupdate", t.getDateupdated());
        sd.insert("Tasks",null,cv);
    }

    public Cursor getdata() {

        SQLiteDatabase sd = this.getReadableDatabase();
        return sd.rawQuery("Select * From Tasks",null);
    }

    public Cursor getpendingdata() {
        SQLiteDatabase sd = this.getReadableDatabase();
        return sd.rawQuery("Select * From Tasks where flag = ? ORDER BY pushpin DESC",new String[] {context.getString(R.string.StrPending)});
    }

    public Cursor getComplatedata() {
        SQLiteDatabase sd = this.getReadableDatabase();
        return sd.rawQuery("Select * From Tasks where flag = ? ORDER BY pushpin DESC",new String[] {context.getString(R.string.StrComplate)});
    }


    public void updateData(Tasks t) {
        SQLiteDatabase sd  = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", t.getTitle());
        cv.put("description", t.getDescription());
        cv.put("flag", t.getFlag());
        if (!t.getDatecreated().isEmpty()){
            cv.put("datecreate", t.getDatecreated());
        }
        cv.put("dateupdate", t.getDateupdated());
        sd.update("Tasks",cv," id = ?", new String[] {String.valueOf(t.getID())});
    }
    public void updatepushpin(int id, boolean push){
        SQLiteDatabase sd  = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pushpin",push? 1 : 0);
        sd.update("Tasks",cv," id = ?", new String[] {String.valueOf(id)});
    }

    public void deletedata(Tasks t){
        SQLiteDatabase sd  = this.getWritableDatabase();
        sd.delete("Tasks"," id = ?", new String[] {String.valueOf(t.getID())});
    }



}
