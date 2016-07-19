package com.myapplication2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class IntroActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


    }

    public void openPage(View view)
    {
        String button_text;
        button_text = ((Button) view).getText().toString();
        EditText name1;
        String beacon1;
        name1 = (EditText) findViewById(R.id.child1);
        beacon1 = name1.getText().toString();

        if(button_text.equals("Next"))
        {
            Intent intent = new Intent (this, MainActivity.class);
            intent.putExtra("beacon1", beacon1);
            startActivity(intent);
        }
    }


}
