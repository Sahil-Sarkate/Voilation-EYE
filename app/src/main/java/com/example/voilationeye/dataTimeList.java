package com.example.voilationeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dataTimeList extends AppCompatActivity {
    ListView l;
    String[] dateTime = new String[] {
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_time_list);
        l = findViewById(R.id.list);
        if (MainActivity.flag == true)
        {
            final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(dateTime));
            final ArrayAdapter<String> adapter = new ArrayAdapter<>
                    (dataTimeList.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
            l.setAdapter(adapter);
            ListElementsArrayList.add(MainActivity.timestamp);
            adapter.notifyDataSetChanged();

        }else{
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }
}