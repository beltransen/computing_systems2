package com.uc3m.beltransen.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewListActivity extends AppCompatActivity {

    private EditText listNameEditText;
    private Button newListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        listNameEditText = (EditText) findViewById(R.id.list_name);
        newListButton = (Button) findViewById(R.id.btn_newlist);

        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = listNameEditText.getText().toString();
                if (name.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Checklist name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent newTask = new Intent();
                newTask.putExtra("name", name);
                setResult(NewListActivity.RESULT_OK, newTask);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Log.d("NewListActivity","Home pressed");
            setResult(NewListActivity.RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        Log.d("NewListActivity","Back button pressed");
        setResult(NewListActivity.RESULT_CANCELED);
        super.onBackPressed(); // This calls finish
    }
}
