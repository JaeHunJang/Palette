package jjh.com.palette;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DBHelper(Context context){
        super(context,"groupDB",null,1);
        //factory > customizing cursor 를 사용할 때 사용
        //version > DB의 version
    }
    @Override
    public void onCreate(SQLiteDatabase db) {//테이블 생성
        db.execSQL("CREATE TABLE Account (id VARCHAR2(20) PRIMARY KEY, pw VARCHAR2(20), nick VARCHAR2(20), birth VARCHAR2(20), hint VARCHAR2(20));"); //회원정보 테이블
        db.execSQL("CREATE TABLE Library (id VARCHAR2(20), library VARCHAR2(20), FOREIGN KEY(id) REFERENCES Account(id) );"); //회원의 라이브러리 정보를 가진 테이블
        db.execSQL("CREATE TABLE Theme (library VARCHAR2(20), name VARCHAR2(20), color VARCHAR2(20), date VARCHAR2(20), tags VARCHAR2(60), FOREIGN KEY(library) REFERENCES Library(library));");

        //db.execSQL("CREATE TABLE HashTags (tagName VARCHAR2(20));"); //태그 종류를 가진 테이블

        /*db.execSQL("CREATE TABLE Theme (library VARCHAR2(20), name VARCHAR2(20), color VARCHAR2(20)" +
                ", date VARCHAR2(20), tag1 VARCHAR2(20), tag2 VARCHAR2(20), tag3 VARCHAR2(20)" +
                ", FOREIGN KEY(library) REFERENCES Library(library)" +
                ", FOREIGN KEY(tag1) REFERENCES HashTags(tagName)" +
                ", FOREIGN KEY(tag2) REFERENCES HashTags(tagName)" +
                ", FOREIGN KEY(tag3) REFERENCES HashTags(tagName));");
                */
        /*
        insert("TagName", "'따뜻함'");
        insert("TagName", "'차가움'");
        insert("TagName", "'산뜻함'");
        insert("TagName", "'어두운'");
        insert("TagName", "'밝은'");
        insert("TagName", "'무더운'");
        insert("TagName", "'붉은'");
        insert("TagName", "'푸른'");
        insert("TagName", "'노란'");
        insert("TagName", "'초록'");
        insert("TagName", "'청록'");
        insert("TagName", "'자홍'");
        insert("TagName", "'봄'");
        insert("TagName", "'여름'");
        insert("TagName", "'가을'");
        insert("TagName", "'겨울'");
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(db);
    }

    public void insert(String insert_into, String values){
        db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + insert_into +" VALUES (" + values + ");");
        db.close();
    }

    public void update(String update, String set){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE " + update +" SET " + set + ";");
        db.close();
    }

    public void update(String update, String set, String where){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE " + update +" SET " + set + " WHERE " + where + ";");
        db.close();
    }

    public void delete(String delete_from, String where){
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + delete_from +" WHERE " + where  + ";");
        db.close();
    }

    public ArrayList[] select(String select_from, String where){
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + select_from + " WHERE " + where + ";",null);
        ArrayList[] arrayList = new ArrayList[cursor.getCount()];
        if (cursor.getCount() != 0) {
            for (int i = 0; cursor.moveToNext() ; i++){
                arrayList[i] = new ArrayList();
                for (int j = 0; j < cursor.getColumnCount(); j++)
                arrayList[i].add(cursor.getString(j));
            }
        }
        cursor.close();
        db.close();
        return  arrayList;
    }
}
