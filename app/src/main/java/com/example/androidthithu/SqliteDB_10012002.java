package com.example.androidthithu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqliteDB_10012002 extends SQLiteOpenHelper {
    private static final String TableName="Taxi_01";
    private static final String Id="id";
    private static final String CarNumber="CarNumber";
    private static final String Distance="Distance";
    private static final String Cost="Cost";
    private static final String Sale="Sale";

    public SqliteDB_10012002(@Nullable Context context,
                             @Nullable String name,
                             @Nullable SQLiteDatabase.CursorFactory factory,
                             int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate="Create table if not exists "+TableName+ "("+ Id+" Integer Primary key AUTOINCREMENT,"
                +CarNumber+ " Text," + Distance + " Real, "+Cost + " integer, "+Sale +" integer)";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //xoa bang table
        sqLiteDatabase.execSQL("Drop table if exists "+TableName);
        //tao lai
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Taxi> getAllTaxi(){
        ArrayList<Taxi> listTaxi= new ArrayList<>();
        //cau truy van
        String sql ="select * from "+TableName;
        //lay doi tg csdl
        SQLiteDatabase db=this.getReadableDatabase();
        //chay cau truy van
        Cursor cursor=db.rawQuery(sql,null);
        //Tao arraylis taxi de tra ve
        if(cursor!=null){
            while(cursor.moveToNext()){
                Taxi taxi= new Taxi(cursor.getInt(0),cursor.getString(1),
                        cursor.getDouble(2),cursor.getInt(3),cursor.getInt(4));
                listTaxi.add(taxi);
            }
        }
        return listTaxi;
    }
    public void addTaxi(Taxi taxi){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
//        values.put(Id,taxi.getMaId());
        values.put(CarNumber,taxi.getSoXe());
        values.put(Distance,taxi.getQuangDuong());
        values.put(Cost,taxi.getDonGia());
        values.put(Sale,taxi.getKhuyenMai());
        long id=db.insert(TableName,null,values);
        if(id!=-1){
            taxi.setMaId((int) id);
        }
        db.close();
    }

    public void clearData(){
        String sql="Delete from "+ TableName;
        SQLiteDatabase  db= this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void deleteTaxi(Taxi taxi){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TableName,Id+"=?",new String[]{String.valueOf(taxi.getMaId())});
        db.close();
    }

    public void upDateTaxi(Taxi taxi){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(Id,taxi.getMaId());
        values.put(CarNumber,taxi.getSoXe());
        values.put(Distance,taxi.getQuangDuong());
        values.put(Cost,taxi.getDonGia());
        values.put(Sale,taxi.getKhuyenMai());
        db.update(TableName,values,Id+"=?",new String[]{String.valueOf(taxi.getMaId())});
        db.close();
    }
}
