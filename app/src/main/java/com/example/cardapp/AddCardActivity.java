package com.example.cardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent( AddCardActivity.this, MainActivity.class);
                AddCardActivity.this.startActivityForResult(intent, 100);
                finish();
            }
        });

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("Question", ((EditText) findViewById(R.id.AddCardActivity_question)).getText().toString());
                intent.putExtra("Answer", ((EditText) findViewById(R.id.AddCardActivity_answer)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ((EditText) findViewById(R.id.AddCardActivity_question)).getText().toString();
        ((EditText) findViewById(R.id.AddCardActivity_answer)).getText().toString();
    }


}

