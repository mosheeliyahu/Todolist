package il.ac.huji.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class TodoListManagerActivity extends AppCompatActivity {
    private static final int deleteMenuID = 2;
    private static final int callMenuID = 1;
    private ArrayList<TodoObject> todoItems;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        todoItems = new ArrayList<TodoObject>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = (ListView)findViewById(R.id.lstTodoItems);
        todoAdapter = new TodoAdapter(getApplicationContext(),R.layout.list_layout, todoItems);
        list.setAdapter(todoAdapter);
        registerForContextMenu(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItemAdd) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(TodoListManagerActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.add_menu_layout, null);
            builder.setView(dialogLayout);
            builder.setTitle(R.string.AddMenuTitle);
            builder.setPositiveButton(R.string.AddMenuOK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    EditText itemAdd = (EditText) dialogLayout.findViewById(R.id.edtNewItem);
                    DatePicker date = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
                    todoItems.add(new TodoObject(itemAdd.getText().toString(),date.getYear(),
                            date.getMonth(),date.getDayOfMonth()));
                    todoAdapter.notifyDataSetChanged();
                    itemAdd.setText("");
                }
            });
            builder.setNegativeButton(R.string.AddMenuCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {}
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()== R.id.lstTodoItems) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            String title=todoItems.get(info.position).getTitle();
            menu.setHeaderTitle(title);
            menu.add(Menu.NONE, deleteMenuID, deleteMenuID, R.string.contaxtMenuDelete);
            if(title.toLowerCase().contains(getResources().getString(R.string.callTrigger))){
                menu.add(Menu.NONE, callMenuID, callMenuID,
                        getResources().getString(R.string.callMenu) + " " + todoItems.get(info.position).getTelNumber());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int choice = item.getItemId();
        switch (choice){
            case deleteMenuID:
                todoItems.remove(info.position);
                todoAdapter.notifyDataSetChanged();
                break;
            case callMenuID:
                Intent dial = new Intent(Intent.ACTION_DIAL,Uri.parse(
                        getResources().getString(R.string.telephone) + todoItems.get(info.position).getTelNumber()));
                startActivity(dial);
                break;
        }
        return true;
    }
}
