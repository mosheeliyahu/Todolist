package il.ac.huji.todolist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by moshe on 12/03/2016.
 */

public class TodoAdapter extends ArrayAdapter<TodoObject> {
        public TodoAdapter(Context context,int layout, ArrayList<TodoObject> todoItems) {
            super(context, layout, todoItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_layout, null);
            TextView rowTitle = (TextView)view.findViewById(R.id.row_title);
            TextView rowDate = (TextView)view.findViewById(R.id.row_date);
            if(getItem(position).hasPassed()){
                rowTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorListRed));
                rowDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorListRed));
            }
            else{
                rowTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorListBlack));
                rowDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorListBlack));
            }
            rowTitle.setText(getItem(position).getTitle());
            rowDate.setText(getItem(position).getDate());
            return view;
        }
}
