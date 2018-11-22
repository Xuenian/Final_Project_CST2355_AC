package ca.algonquinstudents.cst2335_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Member3MainActivity extends AppCompatActivity {

    private ToolbarMenu toolitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member3_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarm3);
        setSupportActionBar(toolbar);

        toolitem = new ToolbarMenu(Member3MainActivity.this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(2).setVisible(false);
        toolitem.setHelpTitle(getString(R.string.m3_help_title));
        toolitem.setHelpMessage(getString(R.string.m3_help_message));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        intent = toolitem.onToolbarItemSelected(item);
        if( intent != null) {
            startActivity(intent);
            Member3MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
