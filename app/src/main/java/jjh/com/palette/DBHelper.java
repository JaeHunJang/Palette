package jjh.com.palette;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DBHelper(Context context){
        super(context,"palette",null,1);
        //factory > customizing cursor 를 사용할 때 사용
        //version > DB의 version
    }
    @Override
    public void onCreate(SQLiteDatabase db) throws SQLException {//테이블 생성
        db.execSQL("CREATE TABLE Account (id VARCHAR2(20) PRIMARY KEY, pw VARCHAR2(20), birth VARCHAR2(20), hint VARCHAR2(20));"); //회원정보 테이블
        db.execSQL("CREATE TABLE Library (id VARCHAR2(20), library VARCHAR2(20),PRIMARY KEY(id,library), FOREIGN KEY(id) REFERENCES Account(id) ON DELETE CASCADE);"); //회원의 라이브러리 정보를 가진 테이블
        db.execSQL("CREATE TABLE Theme (id VARCHAR2(20), library VARCHAR2(20), name VARCHAR2(20), color VARCHAR2(60), date VARCHAR2(20), tags VARCHAR2(60), FOREIGN KEY(id,library) REFERENCES Library(id,library) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS palette");
        onCreate(db);
    }

    public void insert(String insert_into, String values) throws SQLException{ //데이터 삽입
        db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + insert_into +" VALUES (" + values + ");");
        db.close();
    }

    public void update(String update, String set, String where) throws SQLException{ //데이터 업데이트
        db = this.getWritableDatabase();
        db.execSQL("UPDATE " + update +" SET " + set + " WHERE " + where + ";");
        db.close();
    }

    public void delete(String delete_from, String where) throws SQLException{ //데이터 삭제
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + delete_from +" WHERE " + where  + ";");
        db.close();
    }

    public ArrayList[] select(String select_from, String where) throws SQLException{ //데이터 검색
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
    void getError(SQLException sqle){
        Log.d("SQLException",sqle.getMessage());
    } //디비 에러 메시지 출력
}
