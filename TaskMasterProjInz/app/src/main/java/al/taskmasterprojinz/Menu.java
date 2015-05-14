package al.taskmasterprojinz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by Agnieszka on 2015-05-09.
 */
public class Menu extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        /*ActionBar bar = getActionBar();
        assert bar != null;
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i=1; i <= 3; i++) {
            ActionBar.Tab tab = bar.newTab();
            tab.setText("Tab " + i);
            //tab.setTabListener(this);
            bar.addTab(tab);

        }

*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.my_tasks :
                //przejscie do aktywnosci moje zadania
                myTasks();
                break;
            case R.id.sended_tasks :
                //przejscie do aktywnosci ze zleconymi zadaniami
                sendedTasks();
                break;
            case R.id.group_tasks :
                //przejscie do aktywnosci z grupowymi zadaniami
                groupTasks();
                break;
            case R.id.synchronize :
                //synchronizacja danych
                synchronize();
                break;
            case R.id.settings :
                //przejscie do aktywnosci z ustawieniami
                settings();
                break;
            case R.id.help :
                //przejscie do aktywnosci z pomocą
                help();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void myTasks(){
        Intent myTasks = new Intent(getApplicationContext(), MainView.class);
        startActivity(myTasks);
    }
    public void sendedTasks(){

    }
    public void groupTasks(){

    }
    public void synchronize(){

    }
    public void settings(){

    }
    public void help(){

    }


}