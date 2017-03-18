package com.amrit.e_ttend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this,StudentNav.class));
        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            startActivity(new Intent(this,StudentNav.class));
            finish();
            return;
        }
       Button studentbutton = (Button) findViewById(R.id.studentbutton);
        studentbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,StudentLogin.class));
                    finish();
            }
        });
        Button teacherbutton = (Button) findViewById(R.id.teacherbutton);
        teacherbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
// Click event trigger here
            }
        });
    }
}
