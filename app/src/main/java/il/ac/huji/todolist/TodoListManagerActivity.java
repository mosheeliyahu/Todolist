package il.ac.huji.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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


public class TodoListManagerActivity extends AppCompatActivity {
    private static final int deleteMenuID = 2;
    private static final int callMenuID = 1;
    private DBHelper db;
    private TodoCursorAdapter cursorTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = (ListView)findViewById(R.id.lstTodoItems);
        db = DBHelper.getInstance(getApplicationContext());
        cursorTodoAdapter = new TodoCursorAdapter(getApplicationContext(),null ,0);
        list.setAdapter(cursorTodoAdapter);
        registerForContextMenu(list);
        updateDB();
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
                    if(!itemAdd.getText().toString().isEmpty()) {
                        DatePicker date = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
                        addToDB(itemAdd.getText().toString(), date.getDayOfMonth(), date.getMonth(), date.getYear());
                        //itemAdd.setText("");
                    }
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
            Cursor cursor = (cursorTodoAdapter).getCursor();
            cursor.moveToPosition(info.position);
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            menu.setHeaderTitle(title);
            menu.add(Menu.NONE, deleteMenuID, deleteMenuID, R.string.contaxtMenuDelete);
            if(title.toLowerCase().contains(getResources().getString(R.string.callTrigger))){
                menu.add(Menu.NONE, callMenuID, callMenuID,
                        getResources().getString(R.string.callMenu) + " " + getTelNumber(title));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int choice = item.getItemId();
        Cursor cursor = cursorTodoAdapter.getCursor();
        cursor.moveToPosition(info.position);
        switch (choice){
            case deleteMenuID:
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                deleteFromDB(id);
                break;
            case callMenuID:
                String title = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                Intent dial = new Intent(Intent.ACTION_DIAL,Uri.parse(
                        getResources().getString(R.string.telephone) + getTelNumber(title)));
                startActivity(dial);
                break;
        }
        return true;
    }

    private String getTelNumber(String title){
        return title.replaceAll("[^\\d-]", "").trim();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void updateDB(){
        Thread t = new Thread(
                new Runnable() {
                    public void run() {
                        final Cursor c = db.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cursorTodoAdapter.changeCursor(c);
                                cursorTodoAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
        );
        t.start();
    }

    private void addToDB(final String title,final int day,final int month,final int year){
        Thread t = new Thread(
                new Runnable() {
                    public void run() {
                        db.insertToDo(title,day,month,year);
                        updateDB();
                    }
                }
        );
        t.start();
    }

    private void deleteFromDB(final int id) {
        Thread t = new Thread(
                new Runnable() {
                    public void run() {
                        db.delete(id);
                        updateDB();
                    }
                }
        );
        t.start();
    }
}
