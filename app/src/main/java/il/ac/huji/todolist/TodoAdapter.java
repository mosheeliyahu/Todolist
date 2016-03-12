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


public class TodoAdapter extends ArrayAdapter<String> {
        public TodoAdapter(Context context,int layout, ArrayList<String> todoItems) {
            super(context, layout, todoItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_layout, null);
            TextView rowItem = (TextView)view.findViewById(R.id.row);
            if(position%2==0){
                rowItem.setTextColor(ContextCompat.getColor(getContext(), R.color.colorListPink));
            }
            else{
                rowItem.setTextColor(ContextCompat.getColor(getContext(), R.color.colorListGreen));
            }
            rowItem.setText(getItem(position));
            return view;
        }
}
