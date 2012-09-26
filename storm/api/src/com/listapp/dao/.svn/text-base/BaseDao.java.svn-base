package com.listapp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.listapp.model.ModelBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T extends ModelBase> {

    protected SQLiteDatabase db;
    
    protected Class<T> clazz;

    public BaseDao(Context ctx) {
        // Reflection voodoo to get the type parameter from the subclass 
        Type genericSuperclass = getClass().getGenericSuperclass();
        // Allow this class to be safely instantiated with or without a parameterized type
        if (genericSuperclass instanceof ParameterizedType)
            clazz = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        
        Database database = new Database(ctx);
        this.db = database.getWritableDatabase();
        // TODO don't drop database every time
        database.onUpgradeDropTablesAndCreate(db);
    }
    
    protected String getTableName() {
        return Database.getTableName(clazz);
    }

    public long put(T obj) {
        long id = db.insertOrThrow(this.getTableName(), null, obj.getEditableValues());
        return id;
    }
    
    public List<T> listAll() {
        ArrayList<T> list = new ArrayList<T>();
        Cursor cursor = db.query(getTableName(), null, null, null, null, null, null);
        for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
            T obj = newInstance(cursor);
            list.add(obj);
        }
        return list;
    }
    
    protected T newInstance(Cursor c) {
        Constructor<T> constructor;
        T obj = null;
        try {
            constructor = clazz.getConstructor(Cursor.class);
            obj = constructor.newInstance(c);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }

    protected T newInstance() {
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }
}
