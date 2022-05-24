package com.example.project03_timekeeping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.adapter.ShowOneMemberAdapter;
import com.example.project03_timekeeping.database.Database;
import com.example.project03_timekeeping.object.OneMember;

import java.util.ArrayList;
import java.util.List;

public class ShowOneMemberActivity extends AppCompatActivity {
    TextView tvName, tvDate, tvCount;
    RecyclerView rcvShowOneMember;
    List<OneMember> oneMemberList;
    ShowOneMemberAdapter adapter;
    Database database;
    String name;
    ImageView imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_member);
        mapping();
        Intent intent = getIntent();
        String month = intent.getStringExtra("month");
        name = intent.getStringExtra("name");
        int count = intent.getIntExtra("count", 0);
        tvName.setText(name);
        tvDate.setText(month);
        tvCount.setText(String.valueOf(count));

        adapter = new ShowOneMemberAdapter(this, name);
        getData(name, month);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvShowOneMember.setLayoutManager(linearLayoutManager);
        adapter.setData(oneMemberList);
        rcvShowOneMember.setAdapter(adapter);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowOneMemberActivity.this, MainActivity.class));
            }
        });

    }
    private void mapping(){
        tvName = findViewById(R.id.tvNameShowOneMember);
        tvDate = findViewById(R.id.tvDateShowOneMember);
        rcvShowOneMember = findViewById(R.id.rcvShowOneMember);
        tvCount = findViewById(R.id.tvCountShowOneMember);
        imgHome = findViewById(R.id.imgHomeShowOneMember);
        oneMemberList = new ArrayList<>();
        database = new Database(this);
    }
    private void getData(String name, String month){
        Cursor data = database.getData("SELECT morning, evening, alls, date, note FROM Info WHERE name = '"+name+"' and month = '"+month+"'");
        while (data.moveToNext()){
            String note = data.getString(4);
            String date = data.getString(3);
            int mor = data.getInt(0);
            int evening = data.getInt(1);
            int alls = data.getInt(2);
            String time = "";
            if(mor == 1){
                time = "Morning";
            }else if(evening == 1){
                time = "Evening";
            }else if(alls == 2){
                time = "All";
            }else {
                time = "Absent";
            }
            oneMemberList.add(new OneMember(date, time, note));
        }
    }
}