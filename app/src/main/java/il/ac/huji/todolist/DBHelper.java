package il.ac.huji.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper{
    private static DBHelper singletonDB;
    public static final String TABLE_NAME = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";


    private DBHelper(Context context){
        super(context, context.getString(R.string.db_name),null,1);
    }


    public static DBHelper getInstance(Context context){
        if(singletonDB==null){
            singletonDB = new DBHelper(context);
        }
        return singletonDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_DATE + " string, " + COLUMN_NAME + " string );" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertToDo(String name, int day,int month,int year)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        Calendar dueDate = Calendar.getInstance();
        dueDate.set(year, month, day);
        contentValues.put(COLUMN_DATE, TodoCursorAdapter.formatter.format(dueDate.getTime()));
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from "+TABLE_NAME, null );
    }

    public boolean delete(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=" + Integer.toString(id), null) > 0;
    }

}
