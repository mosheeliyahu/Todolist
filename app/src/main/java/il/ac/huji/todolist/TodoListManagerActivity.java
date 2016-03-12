package il.ac.huji.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.LinkedList;

public class TodoListManagerActivity extends AppCompatActivity {
    private static final int deleteMenuID = 1;
    private ArrayList<String> todoItems;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        todoItems = new ArrayList<String>();
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
            EditText itemAdd=(EditText)findViewById(R.id.edtNewItem);
            if(!itemAdd.getText().toString().isEmpty()){
                todoItems.add(itemAdd.getText().toString());
                todoAdapter.notifyDataSetChanged();
                itemAdd.setText("");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.lstTodoItems) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(todoItems.get(info.position));
            menu.add(Menu.NONE, deleteMenuID, deleteMenuID, R.string.contaxtMenuDelete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId()== deleteMenuID) {
            todoItems.remove(info.position);
            todoAdapter.notifyDataSetChanged();
        }
        return true;
    }
}
