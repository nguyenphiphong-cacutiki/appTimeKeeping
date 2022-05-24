package com.example.project03_timekeeping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.activity.ShowAllActivity;
import com.example.project03_timekeeping.database.Database;
import com.example.project03_timekeeping.object.OneMember;

import java.util.List;

public class ShowOneMemberAdapter extends RecyclerView.Adapter<ShowOneMemberAdapter.Holder> {
    private Context context;
    List<OneMember> list;
    private String name;
    private Database database;

    public ShowOneMemberAdapter(Context context, String name){
        database = new Database(context);
        this.name = name;
        this.context = context;
    }
    public void setData(List<OneMember> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_one_member, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        OneMember oneMember = list.get(position);
        if(oneMember == null) return;
        holder.tvDate.setText(oneMember.getDate());
        holder.tvTime.setText(oneMember.getTime());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOneDate(name, oneMember.getDate(), holder.getAdapterPosition());
            }
        });
        holder.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, oneMember.getNote(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvDate, tvTime;
        ImageView imgDelete;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateItemShowOne);
            tvTime = itemView.findViewById(R.id.tvTimeShowOneMember);
            imgDelete = itemView.findViewById(R.id.imgDeleteItemShowOne);
        }
    }
    private void deleteOneDate(String name, String date, int pos){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Are you sure delete "+ date+ " ?");
        dialog.setIcon(android.R.drawable.ic_delete);
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.queryData("DELETE FROM Info WHERE name = '"+name+"' and date = '"+date+"'");
                if(list.size() == 1){
                    context.startActivity(new Intent(context, ShowAllActivity.class));
                }else{
                    list.remove(pos);
                    notifyDataSetChanged();
                }
            }
        });
        dialog.show();
    }
}
