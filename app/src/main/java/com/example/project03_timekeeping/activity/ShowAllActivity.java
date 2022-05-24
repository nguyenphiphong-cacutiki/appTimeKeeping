package com.example.project03_timekeeping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.adapter.ShowAllAdapter;
import com.example.project03_timekeeping.database.Database;
import com.example.project03_timekeeping.object.Member;
import com.example.project03_timekeeping.object.MonthData;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {
    RecyclerView rcvShowAll;
    List<MonthData> listMonthData;
    ShowAllAdapter adapter;
    Database database;
    ImageView imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        rcvShowAll = findViewById(R.id.rcvShowAll);
        imgHome = findViewById(R.id.imgHomeShowAll);
        database = new Database(this);
        listMonthData = new ArrayList<>();
        adapter = new ShowAllAdapter(this);
        getData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvShowAll.setLayoutManager(linearLayoutManager);
        adapter.setData(listMonthData);
        rcvShowAll.setAdapter(adapter);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowAllActivity.this, MainActivity.class));
            }
        });
    }
    private void getData(){
        listMonthData.clear();
        List<String> listMonth = new ArrayList<>();
        Cursor months = database.getData("SELECT month FROM Info");
        while (months.moveToNext()){
            String m = months.getString(0);
            if(!isElementExistsInList(listMonth, m)){
                listMonth.add(m);
            }
        }
        for(int i = 0; i < listMonth.size(); i++){
            List<Member> listMember = new ArrayList<>();
            List<String> listName = new ArrayList<>();
            Cursor names = database.getData("SELECT name FROM Info WHERE month = '"+listMonth.get(i)+"'");
            while(names.moveToNext()){
                String name = names.getString(0);
                if(!isElementExistsInList(listName, name)){
                    listName.add(name);
                }
            }
            for(int j = 0; j < listName.size(); j++){
                Cursor countTimeEve = database.getData("SELECT SUM(evening) FROM Info WHERE name = '"+listName.get(j)+"' and month = '"+listMonth.get(i)+"'");
                Cursor countTimeMor = database.getData("SELECT SUM(morning) FROM Info WHERE name = '"+listName.get(j)+"' and month = '"+listMonth.get(i)+"'");
                Cursor countTimeAll = database.getData("SELECT SUM(alls) FROM Info WHERE name = '"+listName.get(j)+"' and month = '"+listMonth.get(i)+"'");
//                Cursor countTimeAbs = database.getData("SELECT SUM(abs) WHERE name = '"+listName.get(j)+"' and month = '"+listMonth.get(i)+"'");
                int count = 0;
                if( countTimeEve.moveToNext() && countTimeAll.moveToNext() && countTimeMor.moveToNext()){
                    count = countTimeAll.getInt(0) + countTimeMor.getInt(0)+ countTimeEve.getInt(0);
                }
                Member member = new Member(listName.get(j), count);
                listMember.add(member);
            }
            MonthData monthData = new MonthData(listMonth.get(i), listMember);
            listMonthData.add(monthData);
        }
    }
    private Boolean isElementExistsInList(List<String> list, String element){
        if (list == null || element == null) return false;
        for (int i = 0; i < list.size(); i++){
            if(element.equals(list.get(i))) return true;
        }
        return false;
    }
}