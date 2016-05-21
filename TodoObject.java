package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//not in use - v3
public class TodoObject {
    private String title;
    private Calendar dueDate;
    public TodoObject(String title,int year,int month,int day){
        dueDate = Calendar.getInstance();
        dueDate.set(year, month, day);
        this.title=title;
    }

    public String getTitle(){
        return title;
    }

    public String getTelNumber(){
        return title.replaceAll("[^\\d-]", "").trim();
    }

    public Boolean hasPassed(){
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE,-1);
        return dueDate.before(yesterday);
    }

    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        return formatter.format(dueDate.getTime());
    }

    public Calendar getDueDate(){
        return dueDate;
    }


}
