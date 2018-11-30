package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Member1MainActivity extends Activity {
    Button favButton, searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member1_main);

        Button Btn1 = findViewById(R.id.member1Btn2);
        Button Btn2 = findViewById(R.id.member1Btn3);
        Button Btn3 = findViewById(R.id.member1Btn4);


        favButton = findViewById(R.id.m1FavButton);
        searchButton = findViewById(R.id.m1SearchButton);

        Btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member1MainActivity.this, Member2MainActivity.class);
                startActivity(intent);
                Member1MainActivity.this.finish();
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member1MainActivity.this, Member3MainActivity.class);
                startActivity(intent);
                Member1MainActivity.this.finish();
            }
        });

        Btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member1MainActivity.this, Member4MainActivity.class);
                startActivity(intent);
                Member1MainActivity.this.finish();
            }
        });


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
}
