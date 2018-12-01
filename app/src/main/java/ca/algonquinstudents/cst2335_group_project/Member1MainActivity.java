package ca.algonquinstudents.cst2335_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Member1MainActivity extends AppCompatActivity {

    private ToolbarMenu toolitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member1_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarm1);
        setSupportActionBar(toolbar);

        toolitem = new ToolbarMenu(Member1MainActivity.this);

        favButton = findViewById(R.id.m1FavButton);
        searchButton = findViewById(R.id.m1SearchButton);

        favButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member1MainActivity.this, M1FavActivity.class);
                startActivity(intent);
                Member1MainActivity.this.finish();
            }
        });








    }


    public void onFavAdded(String name){
        name+=" added to favourites";
        Toast toast = Toast.makeText(Member1MainActivity.this, name, Toast.LENGTH_LONG); //this is the ListActivity
        toast.show(); //display your message box
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(0).setVisible(false);
        toolitem.setHelpTitle(getString(R.string.m1_help_title));
        toolitem.setHelpMessage(getString(R.string.m1_help_message));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        intent = toolitem.onToolbarItemSelected(item);
        if( intent != null) {
            startActivity(intent);
            Member1MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
