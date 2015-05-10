package PreparingData;

import android.content.Context;

import DataModel.MyDate;
import DataModel.Task;
import Database.DbAdapter;

/**
 * Created by Agnieszka on 2015-05-09.
 */
public class CurrentCreatingTask extends Task{

    static CurrentCreatingTask instance;
    DbAdapter db;
    private CurrentCreatingTask(){
        instance = this;
    }

    public static CurrentCreatingTask getInstance(){
        return (instance == null) ? new CurrentCreatingTask() : instance;
    }

    public void cancelTask(){
        instance = new CurrentCreatingTask();
        description = "";
        date_insert = new MyDate();
        date_exec = new MyDate();
        date_archive = new MyDate();
        date_plan_exec = new MyDate();
        date_update = new MyDate();
    }

    public void saveTask(Context context){
        db = DbAdapter.getInstance(context);
        db.insert(this);
        this.cancelTask();

    }

}
