package com.chippy.letsandroid.book_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button search;
    EditText edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt= (EditText) findViewById(R.id.editText);
        search= (Button) findViewById(R.id.button2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edt.getText().toString();
                str.replaceAll("\\s+","%20");
                String hello;
                hello = str.replaceAll("\\s+","%20");
                Intent i = new Intent(MainActivity.this,book.class);
                i.putExtra("key",hello);
                startActivityForResult(i, 1);
            }
        });
    }
}
