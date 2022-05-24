package com.example.project03_timekeeping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.adapter.AddMemberAdapter;
import com.example.project03_timekeeping.adapter.SelectMemberAdapter;
import com.example.project03_timekeeping.database.Database;
import com.example.project03_timekeeping.object.Member;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Database database;
    BottomNavigationView navMain;
    List<Member> getMemberFromDatabase;
    List<Member> selectMemberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mappingAndInitializationVariable();
        navMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuMember: dialogMember();
                        break;
                    case R.id.menuTimekeeping: dialogAddTimekeeping();
                        break;
                    case R.id.menuAll: startActivity(new Intent(MainActivity.this, ShowAllActivity.class));
                }
                return true;
            }
        });

    }
    private void mappingAndInitializationVariable(){
        database = new Database(this);
        navMain = findViewById(R.id.navMain);
        getMemberFromDatabase = new ArrayList<>();
        selectMemberList = new ArrayList<>();
    }
    private void dialogMember(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_management_member);
        // mapping
        EditText etName = dialog.findViewById(R.id.etAddMember);
        RecyclerView rcv = dialog.findViewById(R.id.rcvAddMember);
        Button btnCancel = dialog.findViewById(R.id.btAddMemberCancel);
        Button btnOk = dialog.findViewById(R.id.btAddMemberOk);
        // completed
        // get data
        List<Member> list = new ArrayList<>();
        Cursor data = database.getData("SELECT * FROM Member");
        while (data.moveToNext()){
            list.add(new Member(data.getInt(0), data.getString(1)));
        }
        // set data into rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        AddMemberAdapter adapter = new AddMemberAdapter(this);
        adapter.setData(list);
        rcv.setAdapter(adapter);
        // set function
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please type in name", Toast.LENGTH_SHORT).show();
                }else{
                    database.queryData("INSERT INTO Member VALUES(null, '"+name+"')");
                    // load data
                    list.clear();
                    Cursor data = database.getData("SELECT * FROM Member");
                    while (data.moveToNext()){
                        list.add(new Member(data.getInt(0), data.getString(1)));
                    }
                    adapter.notifyDataSetChanged();
                    etName.setText("");
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void dialogAddTimekeeping(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_timekeeping);
        // mapping
        TextView tvListName = dialog.findViewById(R.id.tvListNameTimeKeeping);
        TextView tvTime = dialog.findViewById(R.id.tvSelectDateAdd);
        EditText etNote = dialog.findViewById(R.id.etNoteAdd);
        RadioButton radMor = dialog.findViewById(R.id.radMor);
        RadioButton radEve = dialog.findViewById(R.id.radEve);
        RadioButton radAll = dialog.findViewById(R.id.radAll);
        RadioButton radAbs = dialog.findViewById(R.id.radAbs);
        Button btCancel = dialog.findViewById(R.id.btTimekeepingCancel);
        Button btOk = dialog.findViewById(R.id.btTimekeepingOk);
        //
        tvListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMemberFromDatabase.clear();
                Cursor data = database.getData("SELECT * FROM Member");
                while (data.moveToNext()){
                    getMemberFromDatabase.add(new Member(data.getInt(0), data.getString(1)));
                }
                dialogSelectMember(tvListName);
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDate(tvTime);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = tvTime.getText().toString().trim();
                String month = date.substring(3);
                String note = etNote.getText().toString().trim();
                int mor = radMor.isChecked()? 1: 0;
                int eve = radEve.isChecked()? 1: 0;
                int all = radAll.isChecked()? 2: 0;
                Boolean abs = radAbs.isChecked();
                if(tvListName.getText().toString().trim().equals("Select name") || date.equals("This is date")){
                    Toast.makeText(MainActivity.this, "Please select date and name", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0 ; i < selectMemberList.size(); i++){
                        Member member = selectMemberList.get(i);
                        Cursor data = database.getData("SELECT id FROM Info WHERE keyName = '"+member.getKeyName()+"' and date = '"+date+"'");
                        if(data.moveToNext()){
                            dialogAskOverrideTimekeeping(member, month,  date, note, mor, eve, all, abs);
                        }else{
                            String sql = "INSERT INTO Info VALUES(null, '"+member.getName()+"', '"+member.getKeyName()+"', '"+mor+"', '"+eve+"', '"+all+"', '"+abs+"', '"+month+"', '"+date+"', '"+note+"')";
                            database.queryData(sql);
                            Log.d("insert", member.getName());
                        }
                    }
                    tvListName.setText("Select name");
                    etNote.setText("");
                }
            }
        });
        dialog.show();
    }
    private void dialogSelectMember(TextView tvListName){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_member);
        //mapping
        Button btCancel = dialog.findViewById(R.id.btCancelSelectMember);
        Button btOk = dialog.findViewById(R.id.btOkSelectMember);
        RecyclerView rcvSelect = dialog.findViewById(R.id.rcvSelectMember);
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvSelect.setLayoutManager(linearLayoutManager);
        SelectMemberAdapter adapter = new SelectMemberAdapter(MainActivity.this);
        adapter.setData(getMemberFromDatabase);
        rcvSelect.setAdapter(adapter);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getMemberFromDatabase.isEmpty()){
                    selectMemberList.clear();
                    for(int i = 0; i < getMemberFromDatabase.size(); i++){
                        if(getMemberFromDatabase.get(i).getCheck()){
                            selectMemberList.add(getMemberFromDatabase.get(i));
                        }
                    }
                    if(!selectMemberList.isEmpty()){
                        StringBuffer listName = new StringBuffer("");
                        for(int i = 0; i < selectMemberList.size() - 1; i++){
                            listName.append(selectMemberList.get(i).getName()).append(", ");
                        }
                        listName.append(selectMemberList.get(selectMemberList.size()-1).getName());
                        tvListName.setText(listName);
                    }else {
                        Toast.makeText(MainActivity.this, "There is no any name selected!", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "There is no any name selected!", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void pickerDate(TextView textView){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                calendar.set(year, month, dayOfMonth);
                String time = format.format(calendar.getTime());
                textView.setText(time);
            }
        }, year, month, day);
        dialog.show();
    }
    private void dialogAskOverrideTimekeeping(Member member, String month, String date, String note, int mor, int eve, int all, Boolean abs){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("this name: " + member.getName()+" have been timekeeping, are you want to override it?");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.queryData("DELETE FROM Info WHERE keyName = '"+member.getKeyName()+"' and date = '"+date+"'");
                String sql = "INSERT INTO Info VALUES(null, '"+member.getName()+"', '"+member.getKeyName()+"', '"+mor+"', '"+eve+"', '"+all+"', '"+abs+"', '"+month+"', '"+date+"', '"+note+"')";
                database.queryData(sql);
            }
        });
        dialog.show();
    }
}