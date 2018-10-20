package ca.algonquinstudents.cst2335_group_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Member2MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member2_main);

        Button Btn1 = findViewById(R.id.member2Btn1);
        Button Btn2 = findViewById(R.id.member2Btn3);
        Button Btn3 = findViewById(R.id.member2Btn4);

        Btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member2MainActivity.this, Member1MainActivity.class);
                startActivity(intent);
                Member2MainActivity.this.finish();
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member2MainActivity.this, Member3MainActivity.class);
                startActivity(intent);
                Member2MainActivity.this.finish();
            }
        });

        Btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Member2MainActivity.this, Member4MainActivity.class);
                startActivity(intent);
                Member2MainActivity.this.finish();
            }
        });
    }
}
