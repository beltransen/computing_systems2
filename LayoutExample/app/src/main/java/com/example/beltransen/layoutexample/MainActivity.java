package com.example.beltransen.layoutexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonAbout, buttonUp, buttonDown;
    TextView textView;
    int counter;

    int ABOUT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;

        textView = (TextView) findViewById(R.id.textView);
        buttonUp = (Button) findViewById(R.id.buttonLeft1);
        buttonDown = (Button) findViewById(R.id.buttonRight1);
        buttonAbout = (Button) findViewById(R.id.buttonCenter2);

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Counter:" + ++counter);
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Counter:" + --counter);
            }
        });

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra("counter", counter);
                startActivityForResult(intent, ABOUT_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ABOUT_CODE && resultCode==1){
            Toast.makeText(MainActivity.this,"Coming back", Toast.LENGTH_SHORT).show();
            if (data!=null){
                counter = data.getIntExtra("counter",0);
                textView.setText("Counter:" + counter);
            }


        }
    }
}
