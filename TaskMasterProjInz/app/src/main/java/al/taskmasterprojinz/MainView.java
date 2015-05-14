package al.taskmasterprojinz;

import android.app.DatePickerDialog;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.Calendar;
import DataModel.MyDate;
import Database.DbAdapter;
import PreparingData.CurrentCreatingTask;
import PreparingData.PrepareListOfTask;
import PreparingViewsAdapter.ExpandableTaskListAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;


public class MainView extends Menu {

    ExpandableTaskListAdapter listAdapter;
    ExpandableListView expListView;

    TextView header;
    ImageButton calendar, edit, remove, menu;

    PrepareListOfTask prepTask;
    boolean standardList = true;
    MyDate filtrDate;

    Intent edit_task_activity;

    DatePickerDialog datePickerDialog;
    CurrentCreatingTask newTask;

    Resources res;

    LocalActivityManager mLocalActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_view);

        //res = getApplicationContext().getResources();
        //initUIElements();
        //initList();

        setContentView(R.layout.activity_tab_host);

        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);



        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        Resources res = getResources();
        tab1.setIndicator(res.getString(R.string.my_task_label_txt));
        tab1.setContent(new Intent(getApplicationContext(),MyTasksView.class));

        tab2.setIndicator(res.getString(R.string.sended_tasks));
        tab2.setContent(new Intent(getApplicationContext(),MyTasksView.class));

        tab3.setIndicator(res.getString(R.string.group_tasks));
        tab3.setContent(new Intent(getApplicationContext(),MyTasksView.class));

        /** Add the tabs  to the TabHost to display. */


        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        tabHost.setCurrentTab(0);

    }

    protected void onResume(){
        super.onResume();
        //initList();
        //listAdapter.notifyDataSetChanged();

        mLocalActivityManager.dispatchResume();
    }

    protected void onRestart(){
        super.onRestart();

        //initList();
        //listAdapter.notifyDataSetChanged();
    }

    protected void onPause(){
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing());
    }


    private void initUIElements(){

        header = (TextView) findViewById(R.id.my_task_label);
        calendar = (ImageButton) findViewById(R.id.calendar_button);
        edit = (ImageButton) findViewById(R.id.add_task_button);
        remove = (ImageButton) findViewById(R.id.clear_task_button);
        menu = (ImageButton) findViewById(R.id.menu_button);

        initOnClickListeners();


    }
    private void initList(){

        prepTask = PrepareListOfTask.getInstance(this);
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        if (standardList){
            listAdapter = prepTask.todayTomorrowInFutureTaskLists();
        }else{


            listAdapter = prepTask.tasksForGivenDate(filtrDate);
        }
        expListView.setAdapter(listAdapter);
        if (listAdapter.canExpandFirstGroup()){
            expListView.expandGroup(0); //mozna rozwijac tylko wtedy kiedy lista nie jest pusta
        }

    }


    private void initOnClickListeners() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.calendar_button:
                        chooseDate();
                        //pierwszy przycisk po lewej
                        break;
                    case R.id.add_task_button:
                        editNewTask();
                        //znak +
                        break;
                    //case R.id.clear_task_button:
                    //    removeTasks();
                        //znak -
                    //    break;
                    case R.id.my_task_label :
                        backToTodayTomorrowFutureView();
                        break;
                    case R.id.menu_button :
                        MainView.this.openOptionsMenu();
                        break;

                }
            }
        };

        calendar.setOnClickListener(onClickListener);
        edit.setOnClickListener(onClickListener);
        //remove.setOnClickListener(onClickListener);
        header.setOnClickListener(onClickListener);
        menu.setOnClickListener(onClickListener);

        registerForContextMenu(remove);
    }

    private void chooseDate(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                MyDate plan_exec = new MyDate(dayOfMonth,monthOfYear + 1, year);
                newTask = CurrentCreatingTask.getInstance();
                newTask.setDate_plan_exec(plan_exec);
                //powinno byc jeszcze wywolanie metody filtrowania zadan
                Intent taskListForChoosenDate = new Intent(getApplicationContext(), MyTasksForDate.class);
                startActivity(taskListForChoosenDate);
                //standardList = false;
                //filtrDate = plan_exec;
                //initList();
                //listAdapter.notifyDataSetChanged();


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    private void editNewTask(){
        edit_task_activity = new Intent(getApplicationContext(), CreateTask.class);
        startActivity(edit_task_activity);
    }



    private void backToTodayTomorrowFutureView(){
        filtrDate = new MyDate();
        standardList = true;
        initList();
        listAdapter.notifyDataSetChanged();
        newTask = CurrentCreatingTask.getInstance();
        newTask.setDate_plan_exec(filtrDate);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.setHeaderTitle(res.getString(R.string.remove_task_menu_header));
        menu.add(0, v.getId(), 0, res.getString(R.string.remove_task_menu_item1));
        menu.add(0, v.getId(), 0, res.getString(R.string.remove_task_menu_item2));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(res.getString(R.string.remove_task_menu_item1))){
            function2(item.getItemId());
        }
        else if(item.getTitle().equals(res.getString(R.string.remove_task_menu_item2))){
            function1(item.getItemId());
        }
        else {return false;}
        return true;
    }
    public void function1(int id){
        Toast.makeText(this, res.getString(R.string.removed_completed_task), Toast.LENGTH_SHORT).show();
        DbAdapter db = DbAdapter.getInstance(getApplicationContext());
        db.deleteCompletedTasks();
        initList();
        listAdapter.notifyDataSetChanged();
    }
    public void function2(int id){
        Toast.makeText(this, res.getString(R.string.removed_all_task), Toast.LENGTH_SHORT).show();
        DbAdapter db = DbAdapter.getInstance(getApplicationContext());
        db.deleteAllTasks();
        initList();
        listAdapter.notifyDataSetChanged();
    }




}

