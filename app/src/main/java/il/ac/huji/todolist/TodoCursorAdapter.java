package il.ac.huji.todolist;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TodoCursorAdapter extends CursorAdapter{

    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    private LayoutInflater cursorInflater;

    public TodoCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView rowTitle = (TextView) view.findViewById(R.id.row_title);
        TextView rowDate = (TextView) view.findViewById(R.id.row_date);
        String title = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
        String date = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE));
        rowTitle.setText(title);
        rowDate.setText(date);

        if(hasPassed(date)){
            rowTitle.setTextColor(ContextCompat.getColor(context, R.color.colorListRed));
            rowDate.setTextColor(ContextCompat.getColor(context, R.color.colorListRed));
        }
        else{
            rowTitle.setTextColor(ContextCompat.getColor(context, R.color.colorListBlack));
            rowDate.setTextColor(ContextCompat.getColor(context, R.color.colorListBlack));
        }
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.list_layout, parent, false);
    }

    private Boolean hasPassed(String date){
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        try {
            return formatter.parse(date).before(yesterday.getTime());
        } catch (Exception e) {
            //System.out.println(e);
            return true;
        }
    }
}
