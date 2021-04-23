package com.example.v4u.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.v4u.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, "ZAPZOO", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE CART ( pid varchar primary key, name varchar, image_url varchar, price integer, quantity integer)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void emptyCart() {
        String query = "DELETE FROM CART";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public List<Product> getAllProduct() {
        List<Product> productList = new ArrayList<>();

        String query = "Select * FROM CART;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setId(cursor.getString(0));
                    Log.d("#####", ""+product.getId());
                    product.setName(cursor.getString(1));
                    product.setImage_url(cursor.getString(2));
                    product.setPrice(Integer.parseInt(cursor.getString(3)));
                    product.setQuantity(Integer.parseInt(cursor.getString(4)));
                    productList.add(product);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        db.close();

        return productList;
    }

    public boolean addToCart(Product product) {
        ContentValues values = new ContentValues();
        values.put("pid", product.getId());
        values.put("name", product.getName());
        values.put("image_url", product.getImage_url());
        values.put("price", product.getPrice());
        values.put("quantity", product.getQuantity());

        SQLiteDatabase db = this.getWritableDatabase();

        long res = db.insert("CART", null, values);
        Log.d("#####", "insert "+res);
        db.close();
        if(res != -1)
        {
            return true;
        } else {
            return false;
        }
    }

    public void addQuantity(Product product) {
        String query = "UPDATE CART SET quantity = "+(product.getQuantity()+1)+" where pid = '"+product.getId()+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void minusQuantity(Product product) {
        String query = "UPDATE CART SET quantity = "+(product.getQuantity()-1)+" where pid = '"+product.getId()+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void removeProduct(Product product) {
        String query = "DELETE FROM CART where pid = '"+product.getId()+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
}
