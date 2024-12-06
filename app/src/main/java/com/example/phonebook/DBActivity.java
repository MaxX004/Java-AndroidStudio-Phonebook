package com.example.phonebook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DBActivity extends AppCompatActivity {
    protected  void SelectSQL(String SelectQ, String[] args, OnSelectSuccess success) throws Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath()+"/contacts.db",null);
        db.beginTransaction();
        Cursor cursor=db.rawQuery(SelectQ, args);

        while(cursor.moveToNext()){
            String ID = cursor.getString(cursor.getColumnIndex("ID"));
            String Name = cursor.getString(cursor.getColumnIndex("Name"));
            String Location = cursor.getString(cursor.getColumnIndex("Location"));
            String Tel1 = cursor.getString(cursor.getColumnIndex("Tel1"));
            String Tel2 = cursor.getString(cursor.getColumnIndex("Tel2"));
            String Tel3 = cursor.getString(cursor.getColumnIndex("Tel3"));

            success.OnElementSelected(ID, Name, Location, Tel1, Tel2, Tel3);
        }
        db.endTransaction();
        db.close();
    }

    protected  void initDB(){
        ExecSQL(
                "CREATE TABLE CONTACTS(" +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null, " +
                        "Location text not null, " +
                        "unique(Tel1)"+
                        "unique(Tel2)"+
                        "unique(Tel3)"+
                        ");",
                null,
                ()->{
                    Toast.makeText(this,"DB was created successfully!",Toast.LENGTH_LONG).show();
                }
                ,
                (error)-> {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
        );
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success, OnError err){
        SQLiteDatabase db = null;

        try{
            db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath()+"/contacts.db",null);

            if(args==null){
                db.execSQL(SQL);
            }else{
                db.execSQL(SQL,args);
            }
            success.OnSuccess();

        }catch (Exception e){
            err.OnQueryError(e.getMessage().toString());

        }finally {
            if(db!=null){
                db.close();
            }
        }
    }

    protected  interface OnQuerySuccess{
        public  void OnSuccess();
    }

    protected  interface  OnError{
        public  void OnQueryError(String error);
    }

    protected  interface  OnSelectSuccess{
        public  void OnElementSelected(String ID, String Name, String Location, String Tel1, String Tel2, String Tel3);
    }
}
